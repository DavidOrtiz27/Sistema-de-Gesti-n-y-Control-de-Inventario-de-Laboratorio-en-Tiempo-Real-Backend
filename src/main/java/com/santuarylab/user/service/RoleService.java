package com.santuarylab.user.service;

import com.santuarylab.user.dto.RoleResponseDTO;
import com.santuarylab.user.entity.RoleEntity;
import com.santuarylab.user.mapper.RoleMapper;
import com.santuarylab.user.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {

    // Se llama a RoleRepository
    private final RoleRepository roleRepository;
    // Se llama a RoleMapper
    private final RoleMapper roleMapper;

    //Lista de roles
    // Se agrega la anotacion @Transactional para que sea de solo lectura
    @Transactional(readOnly = true)
    //se crea el metodo findAll
    public List<RoleResponseDTO> findAll() {
        // se crea el listado de roles
        List<RoleEntity> roles = roleRepository.findAll();

        // se retorna el listado de roles
        return roles.stream().map(roleMapper::toDTO).toList();

    }

    // Busca un rol por su id
    @Transactional(readOnly = true)
    public RoleResponseDTO findById(Long id) {
        RoleEntity role = roleRepository.findById(id).orElseThrow(() -> new RuntimeException("Rol no encontrado con el id: " + id));

        return roleMapper.toDTO(role);
    }

    //Busqueda por nombre
    @Transactional(readOnly = true)
    public RoleResponseDTO findByName(String name) {
        RoleEntity role = roleRepository.findByNameIgnoreCase(name).orElseThrow(() -> new RuntimeException("Rol no encontrado con el nombre: " + name));

        return roleMapper.toDTO(role);
    }

}
