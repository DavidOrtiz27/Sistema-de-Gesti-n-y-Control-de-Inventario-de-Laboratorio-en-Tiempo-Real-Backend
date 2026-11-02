package com.santuarylab.user.controller;


import com.santuarylab.user.dto.RegisterRequestDTO;
import com.santuarylab.user.dto.UsersRequestDTO;
import com.santuarylab.user.dto.UsersResponseDTO;
import com.santuarylab.user.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    public Optional<UsersResponseDTO> getUser(@PathVariable Long id) {
        return usersService.findById(id);
    }

    @GetMapping("/email/{email}")
    public Optional<UsersResponseDTO> getUserByEmail(@PathVariable String email) {
        return usersService.findByEmailIgnoreCase(email);
    }

    @GetMapping("/search")
    public Page<UsersResponseDTO> searchUsers(@RequestParam("q") String search, @PageableDefault(sort = "ASD", size = 10, page = 0) Pageable pageable) {
        return usersService.findByInfo(search, pageable);
    }

    @PostMapping("/newUser")
    public UsersResponseDTO newUser(@RequestBody RegisterRequestDTO registerDTO) {
        return usersService.savePublic(registerDTO);
    }

    @PostMapping("/user/new")
    public UsersResponseDTO newUser(@RequestBody UsersRequestDTO usersDTO) {
        return usersService.savePrivate(usersDTO);
    }
}
