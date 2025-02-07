package aitu.network.aitunetwork.model.entity;

import aitu.network.aitunetwork.model.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Document(collection = "user")
public class User implements Serializable {
    @Id
    private String id;
    private String username;
    private String email;
    @JsonIgnore
    private String password;
    private List<Role> roles;
}