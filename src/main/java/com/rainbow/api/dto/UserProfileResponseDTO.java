package com.rainbow.api.dto;

import java.util.Set;

public record UserProfileResponseDTO(
        Long id,
        String username,
        String fullName,
        boolean graduated,
        Set<String> roles
) {
}
