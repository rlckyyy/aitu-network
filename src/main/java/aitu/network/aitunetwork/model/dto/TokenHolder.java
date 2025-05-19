package aitu.network.aitunetwork.model.dto;

import java.time.LocalDateTime;

public record TokenHolder(
        String token,
        LocalDateTime expiryDate
) {
}
