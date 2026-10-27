package com.rainbow.api.controller;

import com.rainbow.api.dto.AuthLoginRequestDTO;
import com.rainbow.api.dto.AuthResponseDTO;
import com.rainbow.api.service.UserDetailsServiceImp;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = {"/auth", "/api/auth"})
public class AuthenticationController {

    private final UserDetailsServiceImp userDetailsService;

    public AuthenticationController(UserDetailsServiceImp userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login (@RequestBody @Valid AuthLoginRequestDTO userRequest) {
        return new ResponseEntity<>(this.userDetailsService.loginUser(userRequest), HttpStatus.OK);
    }

}
