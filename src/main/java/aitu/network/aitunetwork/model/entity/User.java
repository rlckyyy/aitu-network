package aitu.network.aitunetwork.model.entity;

import aitu.network.aitunetwork.model.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Unwrapped;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Document(collection = "user")
@FieldNameConstants
@EqualsAndHashCode(of = "id", callSuper = false)
public class User extends BaseEntity implements Serializable {
    @Id
    private String id;
    private String username;
    @Indexed(unique = true)
    private String email;
    private String description;
    @JsonIgnore
    private String password;
    private List<Role> roles;
    private List<String> friendList;
    private Avatar avatar;
    @Unwrapped(onEmpty = Unwrapped.OnEmpty.USE_NULL)
    private UserStatusDetails statusDetails;

    public void addFriendList(User user) {
        if (user != null && user.getFriendList() != null) {
            this.friendList.add(user.getId());
            user.getFriendList().add(this.id);
        }
    }
}