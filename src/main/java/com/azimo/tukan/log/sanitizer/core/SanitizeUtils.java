package com.azimo.tukan.log.sanitizer.core;

public class SanitizeUtils {

    public static String sanitizeValue(final String value) {
        if (value == null || value.isEmpty()) {
            return "";
        }

        int ceil = (int)Math.round(value.length() / 3d);
        final int starsLength = Math.max(1, ceil);

        return value.substring(0, ceil)
                + generateStarts(starsLength)
                + value.substring(ceil + starsLength);
    }

    private static String generateStarts(final int length) {
        StringBuilder stars = new StringBuilder();
        for (int i = 0; i < length; i++) {
            stars.append("*");
        }
        return stars.toString();
    }
}
