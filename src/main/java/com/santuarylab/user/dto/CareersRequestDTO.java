package com.santuarylab.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CareersRequestDTO {
    @NotBlank
    @Size(max = 100)
    private String name;
}
