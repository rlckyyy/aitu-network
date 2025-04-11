package aitu.network.aitunetwork.model.entity.chat;

import aitu.network.aitunetwork.model.entity.BaseEntity;
import aitu.network.aitunetwork.model.enums.MessageStatus;
import aitu.network.aitunetwork.model.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(of = "id", callSuper = false)
@Document(collection = "chatMessage")
public class ChatMessage extends BaseEntity implements Serializable {

    @Id
    private String id;
    private String chatId;
    private String senderId;
    private String content;
    private MessageStatus status;
    private MessageType type;
}