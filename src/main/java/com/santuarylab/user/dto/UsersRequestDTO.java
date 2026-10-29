package com.santuarylab.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsersRequestDTO {
    @NotBlank @Size(max = 100)
    private String firstName;

    @NotBlank @Size(max = 100)
    private String lastName;

    @NotBlank @Size(max = 100)
    private String identityDocument;

    @Size(max = 30)
    private String phoneNumber;

    @NotBlank @Email @Size(max = 100)
    private String email;

    @NotBlank @Size(min = 8, max = 72)
    private String password;

    private Long roleId;
    private Long careerId;
}
