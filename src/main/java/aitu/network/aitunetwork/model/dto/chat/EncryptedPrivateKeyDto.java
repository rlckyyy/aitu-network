package aitu.network.aitunetwork.model.dto.chat;

import jakarta.validation.constraints.NotBlank;

public record EncryptedPrivateKeyDto(
        @NotBlank(message = "Encrypted key is required")
        String encryptedKey,

        @NotBlank(message = "Salt is required")
        String salt,

        @NotBlank(message = "IV is required")
        String iv
) {
}