package com.santuarylab.user.controller;

import com.santuarylab.user.dto.CareersRequestDTO;
import com.santuarylab.user.dto.CareersResponseDTO;
import com.santuarylab.user.service.CareersService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/careers")
public class CareersController {
    private final CareersService careersService;

    // Listar todas las carreras (paginado)
    @GetMapping
    public Page<CareersResponseDTO> findAll(@PageableDefault(sort = "name", size = 10) Pageable pageable) {
        return careersService.findAll(pageable);
    }

    // Listar carreras activas (lista completa)
    @GetMapping("/active")
    public List<CareersResponseDTO> findAllActive() {
        return careersService.findAllActive();
    }

    @GetMapping("/{id}")
    public CareersResponseDTO getCareer(@PathVariable Long id) {
        return careersService.findById(id);
    }

    @GetMapping("/name/{name}")
    public CareersResponseDTO getCareerByName(@PathVariable String name) {
        return careersService.findByName(name);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CareersResponseDTO saveCareer(@RequestBody @Valid CareersRequestDTO careersRequestDTO) {
        return careersService.create(careersRequestDTO);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CareersResponseDTO updateCareer(@PathVariable Long id, @RequestBody @Valid CareersRequestDTO careersRequestDTO) {
        return careersService.update(id, careersRequestDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCareer(@PathVariable Long id) {
        careersService.deactivate(id);
    }

}
