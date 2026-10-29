package com.santuarylab.user.mapper;

import com.santuarylab.user.dto.RoleResponseDTO;
import com.santuarylab.user.entity.RoleEntity;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {

    // Para response

    // creamos el mapper para RoleEntity a RoleResponseDTO
    public RoleResponseDTO toDTO(RoleEntity entity) {
        // validamos que el entity no sea null
        if (entity == null) {
            // retornamos null si el entity es null
            return null;
        }

        // creamos el objeto dto
        RoleResponseDTO dto = new RoleResponseDTO();
        // asignamos los valores del entity al dto
        dto.setId(entity.getId());
        // asignamos el nombre del role al dto
        dto.setName(entity.getName());

        return dto;
    };
}
