package aitu.network.aitunetwork.model.entity;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Avatar {
    private String id;
    private String location;
}
