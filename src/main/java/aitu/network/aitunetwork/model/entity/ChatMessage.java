package aitu.network.aitunetwork.model.entity;

import aitu.network.aitunetwork.model.enums.MessageType;
import aitu.network.aitunetwork.model.enums.MessageStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
public class ChatMessage {
   @Id
   private String id;
   private String chatId;
   private String sender;
   private String recipient;
   private String content;
   private Date timestamp;
   private MessageStatus status;
   private MessageType type;
}