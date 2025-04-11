package aitu.network.aitunetwork.service.util;

import aitu.network.aitunetwork.model.dto.chat.NewChatRoomDTO;
import aitu.network.aitunetwork.model.entity.User;
import aitu.network.aitunetwork.model.entity.chat.ChatRoom;
import aitu.network.aitunetwork.model.entity.chat.ChatRoomType;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public class ChatUtils {
    public static String generateOneToOneChatId(List<String> ids) {
        return String.join("_", ids);
    }

    public static String generateChatId(NewChatRoomDTO chatRoom) {
        return chatRoom.chatRoomType().equals(ChatRoomType.ONE_TO_ONE)
                ? generateOneToOneChatId(chatRoom.participantsIds())
                : UUID.randomUUID().toString();
    }

    /**
     * Generates two possible, for example user1 created chat room with user2.
     * At the same time user2 creates chat room with user1.
     * The result of this method helps to find already created chat rooms by chat id's
     * */
    public static List<String> generateTwoPossibleChatIds(@NotNull List<String> ids) {
        if (ids.size() != 2) {
            throw new IllegalArgumentException("chat id's count must be 2");
        }
        return List.of(generateOneToOneChatId(ids), generateOneToOneChatId(ids.reversed()));
    }

    public static String resolveChatRoomTitle(ChatRoom chatRoom, User user) {
        if (chatRoom.getChatRoomType().equals(ChatRoomType.GROUP)) {
            return chatRoom.getTitle();
        } else {
            return chatRoom.getParticipants().stream()
                    .filter(participant -> !participant.getId().equals(user.getId()))
                    .findFirst()
                    .map(User::getUsername)
                    .orElseThrow();
        }
    }
}
