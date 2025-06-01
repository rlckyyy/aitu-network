package aitu.network.aitunetwork.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequest (
        @NotBlank
        String username,
        @Email
        String email,
        @NotBlank
        String password,
        @NotBlank
        String publicKey
) {}