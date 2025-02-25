package aitu.network.aitunetwork.service.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ChatUtils {
    public String generateChatId(String sender, String recipient) {
        return String.format("%s_%s", sender, recipient);
    }
}
