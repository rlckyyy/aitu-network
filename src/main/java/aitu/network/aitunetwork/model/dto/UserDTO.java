package aitu.network.aitunetwork.model.dto;

import jakarta.validation.constraints.Email;

public record UserDTO(
        String username,
        @Email
        String email,
        String password
) {
}
