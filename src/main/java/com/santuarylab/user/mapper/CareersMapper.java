package com.santuarylab.user.mapper;

import com.santuarylab.user.dto.CareersRequestDTO;
import com.santuarylab.user.dto.CareersResponseDTO;
import com.santuarylab.user.entity.CareersEntity;
import org.springframework.stereotype.Component;

@Component
public class CareersMapper {

    // Para response
    public CareersResponseDTO toDTO(CareersEntity entity) {
        if (entity == null) {
            return null;
        }

        CareersResponseDTO dto = new CareersResponseDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setIsActive(entity.getIsActive());

        return dto;
    }

    // Para request
    public CareersEntity toEntity(CareersRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        CareersEntity entity = new CareersEntity();
        entity.setName(dto.getName());

        return entity;
    }


}
