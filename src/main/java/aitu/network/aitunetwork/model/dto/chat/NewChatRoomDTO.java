package aitu.network.aitunetwork.model.dto.chat;

import aitu.network.aitunetwork.model.entity.chat.ChatRoomType;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record NewChatRoomDTO(
        @Size(min = 1, message = "Number of participants must be at least 1") List<@NotBlank(message = "Invalid participants ids") String> participantsIds,
        @NotNull(message = "Chat room type can not be null") ChatRoomType chatRoomType,
        @Nullable String title // title is null when chatRoomType is ONE_TO_ONE
) {
}
