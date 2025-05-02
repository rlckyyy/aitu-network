package aitu.network.aitunetwork.util;

import java.util.Collection;
import java.util.stream.Collectors;

public class ChatUtils {
    public static String generateOneToOneChatId(Collection<String> ids) {
        if (ids.size() != 2) {
            throw new UnsupportedOperationException("Only two ids is valid for one to one chat id");
        }
        return ids.stream()
                .sorted()
                .collect(Collectors.joining("_"));
    }
}
