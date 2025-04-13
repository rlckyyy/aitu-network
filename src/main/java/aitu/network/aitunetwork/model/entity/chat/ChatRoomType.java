package aitu.network.aitunetwork.model.entity.chat;

import aitu.network.aitunetwork.model.entity.User;
import aitu.network.aitunetwork.model.enums.contract.ChatRoomResolver;
import aitu.network.aitunetwork.service.util.ChatUtils;

import java.util.Collection;
import java.util.UUID;

public enum ChatRoomType implements ChatRoomResolver {
    ONE_TO_ONE {
        @Override
        public String resolveTitle(ChatRoom chatRoom, User user) {
            return chatRoom.getParticipants().stream()
                    .filter(participant -> !participant.getId().equals(user.getId()))
                    .findFirst()
                    .map(User::getUsername)
                    .orElseThrow();
        }

        @Override
        public boolean isVisibleForParticipants(ChatRoom chatRoom) {
            return !chatRoom.getEmpty();
        }

        @Override
        public String generateChatId(Collection<String> participantsIds) {
            return ChatUtils.generateOneToOneChatId(participantsIds);
        }
    },
    GROUP {
        @Override
        public String resolveTitle(ChatRoom chatRoom, User user) {
            return chatRoom.getTitle();
        }

        @Override
        public boolean isVisibleForParticipants(ChatRoom chatRoom) {
            return true;
        }

        @Override
        public String generateChatId(Collection<String> participantsIds) {
            return UUID.randomUUID().toString();
        }
    }
}
