package aitu.network.aitunetwork.model.dto;

import jakarta.validation.constraints.Email;

public record LoginRequest(
        @Email
        String email,
        String password) {
}
