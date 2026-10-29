package com.santuarylab.user.mapper;

import com.santuarylab.user.dto.UsersRequestDTO;
import com.santuarylab.user.dto.UsersResponseDTO;
import com.santuarylab.user.entity.UsersEntity;
import org.springframework.stereotype.Component;

@Component
public class UsersMapper {
    private final CareersMapper careersMapper;
    private final RoleMapper roleMapper;


    public UsersMapper(CareersMapper careersMapper, RoleMapper roleMapper) {
        this.careersMapper = careersMapper;
        this.roleMapper = roleMapper;
    }

    // Response
    public UsersResponseDTO toDTO(UsersEntity entity) {
        if (entity == null) {
            return null;
        }

        UsersResponseDTO dto = new UsersResponseDTO();
        dto.setId(entity.getId());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setIdentityDocument(entity.getIdentityDocument());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setEmail(entity.getEmail());
        dto.setRole(roleMapper.toDTO(entity.getRole()));
        dto.setCareer(careersMapper.toDTO(entity.getCareer()));
        return dto;
    }

    // Request
    public UsersEntity toEntity(UsersRequestDTO dto) {

        if (dto == null) {
            return null;
        }

        UsersEntity entity = new UsersEntity();
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setIdentityDocument(dto.getIdentityDocument());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setEmail(dto.getEmail());
        return entity;
    }
}
