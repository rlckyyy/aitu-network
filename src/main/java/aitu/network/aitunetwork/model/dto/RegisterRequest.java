package aitu.network.aitunetwork.model.dto;

public record RegisterRequest (
        String username,
        String email,
        String password
) {}