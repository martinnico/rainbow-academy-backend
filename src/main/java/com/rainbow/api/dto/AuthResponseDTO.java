package com.rainbow.api.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonPropertyOrder({"username", "message", "jwt", "status"})
public record AuthResponseDTO(String username,
                              String message,
                              String jwt,
                              boolean status) {
}
