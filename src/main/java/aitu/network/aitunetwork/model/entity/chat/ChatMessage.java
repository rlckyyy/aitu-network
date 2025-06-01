package aitu.network.aitunetwork.model.entity.chat;

import aitu.network.aitunetwork.model.entity.BaseEntity;
import aitu.network.aitunetwork.model.enums.MessageStatus;
import aitu.network.aitunetwork.model.enums.MessageType;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(of = "id", callSuper = false)
@Document(collection = "chatMessage")
@FieldNameConstants
public class ChatMessage extends BaseEntity implements Serializable {
    @Id
    private String id;
    @NotBlank(message = "errors.400.chats.id")
    private String chatId;
    @NotBlank(message = "errors.400.chats.senders.id")
    private String senderId;
    private Map<String, String> encryptedContent;
    private Map<String, String> encryptedKeys;
    private short length;
    private MessageStatus status;
    private MessageType type;
}