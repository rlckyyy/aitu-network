package aitu.network.aitunetwork.model.dto;

import aitu.network.aitunetwork.model.enums.Role;
import jakarta.validation.constraints.Email;

import java.util.List;

public record UserDTO(
        String id,
        String username,
        @Email
        String email,
        List<Role> roles,
        List<String> friendList
) {
}
