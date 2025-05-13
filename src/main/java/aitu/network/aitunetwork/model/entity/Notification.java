package aitu.network.aitunetwork.model.entity;

import aitu.network.aitunetwork.model.enums.IssuerType;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode(callSuper = false, of = "id")
public class Notification extends BaseEntity {
    @Id
    private ObjectId id;
    private String content;
    private String issuerId;
    private IssuerType issuerType;
    private Boolean isNotified;
}
