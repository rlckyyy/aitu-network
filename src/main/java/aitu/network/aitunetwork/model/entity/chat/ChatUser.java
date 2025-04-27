package aitu.network.aitunetwork.model.entity.chat;


import aitu.network.aitunetwork.model.entity.BaseEntity;
import aitu.network.aitunetwork.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@Document
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class ChatUser extends BaseEntity {
    @Id
    @DBRef(lazy = true)
    private User user;
    private Boolean connected;
    private Boolean showConnectionDetails;
    private Instant leftOn;
    private Instant connectedOn;
}
