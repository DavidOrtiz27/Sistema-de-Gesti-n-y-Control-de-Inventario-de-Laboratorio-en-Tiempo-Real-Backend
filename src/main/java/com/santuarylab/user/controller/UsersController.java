package com.santuarylab.user.controller;


import com.santuarylab.user.dto.*;
import com.santuarylab.user.service.UsersService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsersController {

    private final UsersService usersService;

    @GetMapping
    public List<UsersResponseDTO> getUsers() {
        return usersService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsersResponseDTO> getUser(@PathVariable Long id) {
        return ResponseEntity.of(usersService.findById(id));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UsersResponseDTO> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.of(usersService.findByEmailIgnoreCase(email));
    }

    @GetMapping("/search")
    public Page<UsersResponseDTO> searchUsers(@RequestParam("q") String search,
                                              @PageableDefault(sort = "lastName", size = 10) Pageable pageable) {
        return usersService.findByInfo(search, pageable);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UsersResponseDTO register(@RequestBody @Valid RegisterRequestDTO registerDTO) {
        return usersService.savePublic(registerDTO);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsersResponseDTO create(@RequestBody @Valid UsersRequestDTO usersRequestDTO) {
        return usersService.savePrivate(usersRequestDTO);
    }
}
