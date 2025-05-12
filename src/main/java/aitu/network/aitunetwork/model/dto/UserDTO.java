package aitu.network.aitunetwork.model.dto;

import aitu.network.aitunetwork.model.entity.Avatar;
import aitu.network.aitunetwork.model.entity.UserStatusDetails;
import aitu.network.aitunetwork.model.enums.Role;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String id;
    private String username;
    private String description;
    private @Email String email;
    private Avatar avatar;
    private List<Role> roles;
    private List<String> friendList;
    @JsonUnwrapped
    private UserStatusDetails statusDetails;
}
