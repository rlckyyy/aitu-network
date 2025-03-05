package aitu.network.aitunetwork.model.dto;

import aitu.network.aitunetwork.model.entity.Avatar;
import aitu.network.aitunetwork.model.enums.Role;
import jakarta.validation.constraints.Email;

import java.util.List;

public record UserDTO(
        String id,
        String username,
        String description,
        @Email
        String email,
        Avatar avatar,
        List<Role> roles,
        List<String> friendList,
        String publicKey
) {
}
