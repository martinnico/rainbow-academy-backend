package com.rainbow.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("denyAll()")
public class HelloController {

    @GetMapping("/hellosecured")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @PreAuthorize("hasAuthority('READ')")
    public ResponseEntity<String> helloSecured() {;
        return ResponseEntity.ok("Hello, secured world!");
    }

    @GetMapping("/hellounsecured")
    @PreAuthorize("permitAll()")
    public ResponseEntity<String> helloUnsecured() {
        return ResponseEntity.ok("Hello, unsecured world!");
    }


    @GetMapping("/sayhi")
    @PreAuthorize("permitAll()")
    public String sayHi(){
        return "Hi! This endpoint is not secured";
    }

    @GetMapping("/sayhisec")
    @PreAuthorize("isAuthenticated()")
    public String sayHiSec(){
        return "Hi! This endpoint has been secured";
    }

}

