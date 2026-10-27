package com.rainbow.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthRegisterRequestDTO(
        @NotBlank(message = "El nombre completo es obligatorio") String fullName,
        @NotBlank(message = "El email es obligatorio") @Email(message = "El email no es válido") String username,
        @NotBlank(message = "La contraseña es obligatoria") @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres") String password
) {
}
