package com.azimo.tukan.logging.sanitizer.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SanitizeUtilsTest {

    @Test
    public void testSanitizationOfStrings() {
        Assertions.assertTrue(SanitizeUtils.sanitizeValue(null).equals(""));
        Assertions.assertTrue(SanitizeUtils.sanitizeValue("").equals(""));
        Assertions.assertTrue(SanitizeUtils.sanitizeValue("a").equals("*"));
        Assertions.assertTrue(SanitizeUtils.sanitizeValue("ab").equals("a*"));
        Assertions.assertTrue(SanitizeUtils.sanitizeValue("abc").equals("a*c"));
        Assertions.assertTrue(SanitizeUtils.sanitizeValue("abcd").equals("a*cd"));
        Assertions.assertTrue(SanitizeUtils.sanitizeValue("abcde").equals("ab**e"));
        Assertions.assertTrue(SanitizeUtils.sanitizeValue("abcdef").equals("ab**ef"));
        Assertions.assertTrue(SanitizeUtils.sanitizeValue("abcdefg").equals("ab**efg"));
        Assertions.assertTrue(SanitizeUtils.sanitizeValue("abcdefgh").equals("abc***gh"));
        Assertions.assertTrue(SanitizeUtils.sanitizeValue("abcdefghi").equals("abc***ghi"));
        Assertions.assertTrue(SanitizeUtils.sanitizeValue("abcdefghij").equals("abc***ghij"));
    }
}