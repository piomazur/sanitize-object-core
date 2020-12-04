package com.azimo.tukan.logging.sanitizer.core;

public class SanitizeUtils {

    public static String sanitizeValue(final String value) {
        if (value == null || value.isEmpty()) {
            return "";
        }

        int ceil = (int)Math.round(value.length() / 3d);
        final int index = ceil;
        final int starsLength = Math.max(1, ceil);

        return value.substring(0, index)
                + generateStarts(starsLength)
                + value.substring(index + starsLength);
    }

    private static String generateStarts(final int length) {
        String stars = "";
        for (int i = 0; i < length; i++) {
            stars += "*";
        }
        return stars;
    }
}
