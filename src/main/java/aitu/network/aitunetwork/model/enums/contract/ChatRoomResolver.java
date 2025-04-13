package aitu.network.aitunetwork.model.enums.contract;

import aitu.network.aitunetwork.model.entity.User;
import aitu.network.aitunetwork.model.entity.chat.ChatRoom;

import java.util.Collection;

public interface ChatRoomResolver {
    String resolveTitle(ChatRoom chatRoom, User user);
    boolean isVisibleForParticipants(ChatRoom chatRoom);
    String generateChatId(Collection<String> participantsIds);
}
