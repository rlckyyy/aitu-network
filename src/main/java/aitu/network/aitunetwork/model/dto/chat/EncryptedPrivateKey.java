package aitu.network.aitunetwork.model.dto.chat;

import lombok.*;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@FieldNameConstants
public class EncryptedPrivateKey implements Serializable {
    private String encryptedKey;
    private String salt;
    private String iv;
}