package aitu.network.aitunetwork.model.dto.chat;

import aitu.network.aitunetwork.model.entity.chat.ChatRoomType;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record NewChatRoomDTO(
        @Size(min = 1, message = "errors.400.chats.participants")
        Set<@NotBlank(message = "errors.400.chats.participants.id") String> participantsIds,
        @NotNull(message = "errors.400.chats.rooms.type") ChatRoomType chatRoomType,
        @Nullable String title // title is null when chatRoomType is ONE_TO_ONE
) {
}
