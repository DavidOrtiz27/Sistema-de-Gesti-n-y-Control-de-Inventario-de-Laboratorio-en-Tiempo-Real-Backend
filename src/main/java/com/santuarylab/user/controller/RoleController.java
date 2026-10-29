package com.santuarylab.user.controller;

import com.santuarylab.user.dto.RoleResponseDTO;
import com.santuarylab.user.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    public List<RoleResponseDTO> getRoles() {
        return roleService.findAll();
    }

    @GetMapping("/{id}")
    public RoleResponseDTO getRole(@PathVariable Long id) {
        return roleService.findById(id);
    }

    @GetMapping("/name/{name}")
    public RoleResponseDTO getRoleByName(@PathVariable String name) {
        return roleService.findByName(name);
    }

}
