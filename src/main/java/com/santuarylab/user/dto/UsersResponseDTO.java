package com.santuarylab.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsersResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String identityDocument;
    private String phoneNumber;
    private String email;

    private RoleResponseDTO role;
    private CareersResponseDTO career;
}
