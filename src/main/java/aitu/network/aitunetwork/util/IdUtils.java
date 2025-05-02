package aitu.network.aitunetwork.util;

import java.util.regex.Pattern;

public class IdUtils {

    private static final Pattern HEXADECIMAL_PATTERN = Pattern.compile("\\p{XDigit}+");

    public static boolean isHexFormat(String id) {
        return HEXADECIMAL_PATTERN.matcher(id).matches();
    }

    public static boolean isValidId(String id) {
        return id != null && isHexFormat(id);
    }
}
