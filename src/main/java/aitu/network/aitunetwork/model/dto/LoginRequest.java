package aitu.network.aitunetwork.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record LoginRequest(
        @Email(message = "errors.400.users.email")
        @NotEmpty(message = "errors.400.users.email")
        String email,
        @NotEmpty(message = "errors.400.users.password")
        String password) {
}
