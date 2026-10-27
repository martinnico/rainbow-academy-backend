package com.rainbow.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



public record AuthLoginRequestDTO(@NotBlank String username,
                                  @NotBlank String password) {




}

