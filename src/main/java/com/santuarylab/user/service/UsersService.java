package com.santuarylab.user.service;

import com.santuarylab.user.dto.*;
import com.santuarylab.user.entity.CareersEntity;
import com.santuarylab.user.entity.RoleEntity;
import com.santuarylab.user.entity.RoleName;
import com.santuarylab.user.entity.UsersEntity;
import com.santuarylab.user.mapper.UsersMapper;
import com.santuarylab.user.repository.CareersRepository;
import com.santuarylab.user.repository.RoleRepository;
import com.santuarylab.user.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final UsersRepository usersRepository;
    private final RoleRepository roleRepository;
    private final CareersRepository careersRepository;
    private final UsersMapper usersMapper;
    private final PasswordEncoder passwordEncoder;

    // lista de usuarios
    @Transactional(readOnly = true)
    public List<UsersResponseDTO> findAll() {
        List<UsersEntity> users = usersRepository.findAll();
        return users.stream().map(usersMapper::toDTO).toList();
    }

    // lista por id
    @Transactional(readOnly = true)
    public Optional<UsersResponseDTO> findById(Long id) {
        Optional<UsersEntity> user = usersRepository.findById(id);
        return user.map(usersMapper::toDTO);
    }

    // Busqueda por info Usuario
    @Transactional(readOnly = true)
    public Page<UsersResponseDTO> findByInfo(String info, Pageable pageable) {

        if (info == null || info.isBlank()) {
            Page<UsersEntity> users = usersRepository.findAll(pageable);
            return users.map(usersMapper::toDTO);
        }

        Page<UsersEntity> users = usersRepository.findByInfoUser(info, pageable);
        return users.map(usersMapper::toDTO);
    }

    // Busqueda por email
    @Transactional(readOnly = true)
    public Optional<UsersResponseDTO> findByEmailIgnoreCase(String email) {
        Optional<UsersEntity> user = usersRepository.findByEmailIgnoreCase(email);
        return user.map(usersMapper::toDTO);
    }

    // Crear Usuario PUBLICO
    @Transactional
    public UsersResponseDTO savePublic(RegisterRequestDTO registerDTO) {

        // Validaciones
        if (registerDTO == null) {
            throw new RuntimeException("La informacion del usuario no puede estar vacia.");
        }

        if (identityDocumentExists(registerDTO.getIdentityDocument())) {
            throw new RuntimeException("Este Documento ya se encuentra registrado: " + registerDTO.getIdentityDocument());
        }

        if (emailExists(registerDTO.getEmail())) {
            throw new RuntimeException("Ya existe una cuenta con este correo: " + registerDTO.getEmail());
        }

        // DTO -> Entidad
        UsersEntity user = usersMapper.registerUsers(registerDTO);

        // Buscar en la db el rol
        RoleEntity role = roleRepository.findByNameIgnoreCase(RoleName.STUDENT.name()).orElseThrow(() -> new IllegalStateException("Error del sistema: el rol por defecto no existe en la base de datos."));
        user.setRole(role);

        CareersEntity career = careersRepository.findById(registerDTO.getCareerId()).orElseThrow(() -> new RuntimeException("Error del sistema: la carrera del usuario no existe o es nula."));
        if (!career.getIsActive()) {
            throw new RuntimeException("La carrera seleccionada no está disponible.");
        }
        user.setCareer(career);

        String encodedPassword = passwordEncoder.encode(registerDTO.getPassword());
        user.setPasswordHash(encodedPassword);

        UsersEntity saved = usersRepository.save(user);
        return usersMapper.toDTO(saved);
    }

    // Crear Usuario PRIVADO
    @Transactional
    public UsersResponseDTO savePrivate(UsersRequestDTO usersRequestDTO) {

        // Validaciones
        if (usersRequestDTO == null) {
            throw new RuntimeException("La informacion del usuario no puede estar vacia.");
        }

        if (identityDocumentExists(usersRequestDTO.getIdentityDocument())) {
            throw new RuntimeException("Este Documento ya se encuentra registrado: " + usersRequestDTO.getIdentityDocument());
        }

        if (emailExists(usersRequestDTO.getEmail())) {
            throw new RuntimeException("Ya existe una cuenta con este correo: " + usersRequestDTO.getEmail());
        }

        // DTO -> Entidad
        UsersEntity user = usersMapper.toEntity(usersRequestDTO);

        // Buscar en la db el rol
        RoleEntity role = roleRepository.findById(usersRequestDTO.getRoleId()).orElseThrow(() -> new RuntimeException("Error del sistema: el rol no existe en la base de datos."));
        user.setRole(role);

        String roleName = role.getName();

        if (RoleName.ADMIN.name().equals(roleName)) {
            if (usersRequestDTO.getCareerId() != null) {
                throw new RuntimeException("El rol ADMIN no debe tener una carrera asignada.");
            }
        } else if (RoleName.STUDENT.name().equals(roleName) || RoleName.PROFESSOR.name().equals(roleName)) {
            Long careerId = usersRequestDTO.getCareerId();

            if (careerId == null) {
                throw new RuntimeException("El estudiante o profesor requiere una carrera asignada.");
            }

            CareersEntity career = careersRepository.findById(careerId).orElseThrow(() -> new RuntimeException("La carrera especificada no existe."));
            if (!career.getIsActive()) {
                throw new RuntimeException("La carrera seleccionada no está disponible.");
            }
            user.setCareer(career);
        }

        String encodedPassword = passwordEncoder.encode(usersRequestDTO.getPassword());
        user.setPasswordHash(encodedPassword);

        UsersEntity saved = usersRepository.save(user);
        return usersMapper.toDTO(saved);
    }


    // Update self
    @Transactional
    public UsersResponseDTO updateSelfProfile(Long id, UpdateSelfProfileRequestDTO updateSelfDTO) {
        // Verificar id
        UsersEntity user = usersRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado por id: " + id));

        if (!user.getEmail().equalsIgnoreCase(updateSelfDTO.getEmail()) && emailExists(updateSelfDTO.getEmail())) {
            throw new RuntimeException("Error del sistema: el email ya existe en la base de datos.");
        }
        user.setEmail(updateSelfDTO.getEmail());

        user.setPhoneNumber(updateSelfDTO.getPhoneNumber());

        return usersMapper.toDTO(user);
    }

    @Transactional
    public UsersResponseDTO adminUpdateUser(Long id, AdminUpdateUserRequestDTO adminUpdateUserDTO) {

        UsersEntity user = usersRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado por id: " + id));

        if (!user.getEmail().equalsIgnoreCase(adminUpdateUserDTO.getEmail()) && emailExists(adminUpdateUserDTO.getEmail())) {
            throw new RuntimeException("Error del sistema: el email ya existe en la base de datos.");
        }
        user.setEmail(adminUpdateUserDTO.getEmail());

        // Buscar en la db el rol
        RoleEntity role = roleRepository.findById(adminUpdateUserDTO.getRoleId()).orElseThrow(() -> new RuntimeException("Error del sistema: el rol no existe en la base de datos."));
        user.setRole(role);

        String roleName = role.getName();

        if (RoleName.ADMIN.name().equals(roleName)) {
            if (adminUpdateUserDTO.getCareerId() != null) {
                throw new RuntimeException("El rol ADMIN no debe tener una carrera asignada.");
            }
            user.setCareer(null);
        } else if (RoleName.STUDENT.name().equals(roleName) || RoleName.PROFESSOR.name().equals(roleName)) {
            Long careerId = adminUpdateUserDTO.getCareerId();
            if (careerId == null) {
                throw new RuntimeException("El estudiante o profesor requiere una carrera asignada.");
            }

            CareersEntity career = careersRepository.findById(careerId).orElseThrow(() -> new RuntimeException("La carrera especificada no existe."));
            if (!career.getIsActive()) {
                throw new RuntimeException("La carrera seleccionada no está disponible.");
            }
            user.setCareer(career);
        }

        user.setFirstName(adminUpdateUserDTO.getFirstName());
        user.setLastName(adminUpdateUserDTO.getLastName());
        user.setPhoneNumber(adminUpdateUserDTO.getPhoneNumber());
        user.setIsActive(adminUpdateUserDTO.getIsActive());

        return usersMapper.toDTO(user);
    }

    @Transactional
    public void deactivate(Long id) {
        UsersEntity user = usersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
        user.setIsActive(false);
        usersRepository.save(user);
    }

    protected Boolean identityDocumentExists(String identityDocument) {
        return usersRepository.existsByIdentityDocument(identityDocument);
    }

    protected Boolean emailExists(String email) {
        return usersRepository.existsByEmailIgnoreCase(email);
    }

}
