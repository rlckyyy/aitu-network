package aitu.network.aitunetwork.model.dto.chat;

import aitu.network.aitunetwork.config.security.CustomUserDetails;
import aitu.network.aitunetwork.model.dto.UserShortDTO;
import aitu.network.aitunetwork.model.entity.chat.ChatRoomType;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

public record ChatRoomDTO(String id,
                          String chatId,
                          String title,
                          List<UserShortDTO> participants,
                          ChatRoomType chatRoomType,
                          boolean empty) {

    public ChatRoomDTO {
        if (title == null && chatRoomType.equals(ChatRoomType.ONE_TO_ONE)) {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            CustomUserDetails userDetails = (CustomUserDetails) principal;
            String currentUserId = userDetails.user().getId();

            title = participants.stream().filter(dto -> !dto.getId().equals(currentUserId))
                    .findFirst()
                    .map(UserShortDTO::getUsername)
                    .orElseThrow(() -> new RuntimeException("Companion resolving failed"));
        }
    }
}
