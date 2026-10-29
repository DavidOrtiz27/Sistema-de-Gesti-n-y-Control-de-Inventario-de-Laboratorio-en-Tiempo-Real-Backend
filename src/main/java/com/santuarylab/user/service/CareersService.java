package com.santuarylab.user.service;

import com.santuarylab.user.dto.CareersRequestDTO;
import com.santuarylab.user.dto.CareersResponseDTO;
import com.santuarylab.user.entity.CareersEntity;
import com.santuarylab.user.mapper.CareersMapper;
import com.santuarylab.user.repository.CareersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CareersService {

    private final CareersRepository careerRepository;
    private final CareersMapper careerMapper;

    // listar carreras
    @Transactional(readOnly = true)
    public List<CareersResponseDTO> findAll() {
        List<CareersEntity> careers = careerRepository.findAll();

        return careers.stream()
                .map(careerMapper::toDTO)
                .toList();
    }

    // Buscar por id
    @Transactional(readOnly = true)
    public CareersResponseDTO findById(Long id) {
        CareersEntity career = careerRepository.findById(id).orElseThrow(() -> new RuntimeException("Carrera no encontrada por id: " + id));

        return careerMapper.toDTO(career);
    }

    // Buscar carrera por nombre
    @Transactional(readOnly = true)
    public CareersResponseDTO findByName(String name) {
        CareersEntity career = careerRepository.findByNameIgnoreCase(name).orElseThrow(() -> new RuntimeException("Carrera no encontrada por nombre: " + name));

        return careerMapper.toDTO(career);
    }

    // Crear carrera
    @Transactional
    public CareersResponseDTO create(CareersRequestDTO careerDTO) {
        // Revisar si la carrera ya existe
        if (existsByName(careerDTO.getName())) {
            throw new RuntimeException("Carrera ya existe: " + careerDTO.getName());
        }

        // Guardar la carrera
        CareersEntity career = careerMapper.toEntity(careerDTO);
        CareersEntity saved = careerRepository.save(career);
        return careerMapper.toDTO(saved);
    }

    // Editar Carrera
    @Transactional
    public CareersResponseDTO update(Long id, CareersRequestDTO careerDTO) {

        // Buscar si existe la carrera por el id
        CareersEntity careers = careerRepository.findById(id).orElseThrow(() -> new RuntimeException("Carrera no encontrada por id: " + id));

        // verificar si cambio el nombre o si existe otra carrera con el mismo nombre
        if (!careerDTO.getName().equalsIgnoreCase(careers.getName()) && careerRepository.existsByNameIgnoreCase(careerDTO.getName())) {
            throw new RuntimeException("Carrera ya existe con el nombre: " + careerDTO.getName());
        }

        // Guardar la carrera
        careers.setName(careerDTO.getName());
        return careerMapper.toDTO(careerRepository.save(careers));
    }

    // Soft Delete
    @Transactional
    public void deactivate(Long id) {
        CareersEntity career = careerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Carrera no encontrada con id: " + id));

        career.setIsActive(false);
        careerRepository.save(career);
    }


    // Existe?
    private boolean existsByName(String name) {
        return careerRepository.existsByNameIgnoreCase(name);
    }

}
