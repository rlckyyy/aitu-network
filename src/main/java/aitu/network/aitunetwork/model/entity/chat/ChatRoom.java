package aitu.network.aitunetwork.model.entity.chat;

import aitu.network.aitunetwork.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@Document
public class ChatRoom {
    @Id
    private String id;
    @Indexed(unique = true)
    private String chatId;
    private String title;
    @DBRef(lazy = true)
    private List<User> participants;
    private ChatRoomType chatRoomType;
    private Boolean empty;

    public boolean addParticipant(User participant) {
        return participants.add(participant);
    }

    public boolean removeParticipant(User participant) {
        return participants.remove(participant);
    }
}