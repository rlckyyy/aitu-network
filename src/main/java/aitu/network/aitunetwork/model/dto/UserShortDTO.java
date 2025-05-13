package aitu.network.aitunetwork.model.dto;

import aitu.network.aitunetwork.model.entity.Avatar;
import aitu.network.aitunetwork.model.entity.UserStatusDetails;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public final class UserShortDTO {
    private String id;
    private String username;
    private String email;
    private Avatar avatar;
    @JsonUnwrapped
    private UserStatusDetails statusDetails;
}
