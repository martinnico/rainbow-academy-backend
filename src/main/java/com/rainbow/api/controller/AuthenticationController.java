package com.rainbow.api.controller;

import com.rainbow.api.dto.AuthLoginRequestDTO;
import com.rainbow.api.dto.AuthRegisterRequestDTO;
import com.rainbow.api.dto.AuthResponseDTO;
import com.rainbow.api.dto.UserProfileResponseDTO;
import com.rainbow.api.entity.UserSec;
import com.rainbow.api.exception.ResourceNotFoundException;
import com.rainbow.api.repository.IUserRepository;
import com.rainbow.api.service.UserDetailsServiceImp;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping(path = {"/auth", "/api/auth"})
public class AuthenticationController {

    private final UserDetailsServiceImp userDetailsService;
    private final IUserRepository userRepository;

    public AuthenticationController(UserDetailsServiceImp userDetailsService, IUserRepository userRepository) {
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid AuthLoginRequestDTO userRequest) {
        return new ResponseEntity<>(this.userDetailsService.loginUser(userRequest), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody @Valid AuthRegisterRequestDTO userRequest) {
        return new ResponseEntity<>(this.userDetailsService.registerUser(userRequest), HttpStatus.CREATED);
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponseDTO> getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserSec user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Set<String> roles = user.getRolesList().stream()
                .map(role -> role.getRole())
                .collect(java.util.stream.Collectors.toSet());

        UserProfileResponseDTO response = new UserProfileResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getFullName(),
                user.isGraduated(),
                roles
        );

        return ResponseEntity.ok(response);
    }
}
