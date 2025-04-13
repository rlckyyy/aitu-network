package aitu.network.aitunetwork.service.util;

import java.util.Collection;
import java.util.stream.Collectors;

public class ChatUtils {
    public static String generateOneToOneChatId(Collection<String> ids) {
        return ids.stream()
                .sorted()
                .collect(Collectors.joining("_"));
    }
}
