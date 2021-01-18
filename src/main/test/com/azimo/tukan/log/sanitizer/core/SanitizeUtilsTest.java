package com.azimo.tukan.log.sanitizer.core;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SanitizeUtilsTest {

    @Test
    public void testSanitizationOfStrings() {
        Assertions.assertEquals(SanitizeUtils.sanitizeValue(null), "");
        Assertions.assertEquals(SanitizeUtils.sanitizeValue(""), "");
        Assertions.assertEquals(SanitizeUtils.sanitizeValue("a"), "*");
        Assertions.assertEquals(SanitizeUtils.sanitizeValue("ab"), "a*");
        Assertions.assertEquals(SanitizeUtils.sanitizeValue("abc"), "a*c");
        Assertions.assertEquals(SanitizeUtils.sanitizeValue("abcd"), "a*cd");
        Assertions.assertEquals(SanitizeUtils.sanitizeValue("abcde"), "ab**e");
        Assertions.assertEquals(SanitizeUtils.sanitizeValue("abcdef"), "ab**ef");
        Assertions.assertEquals(SanitizeUtils.sanitizeValue("abcdefg"), "ab**efg");
        Assertions.assertEquals(SanitizeUtils.sanitizeValue("abcdefgh"), "abc***gh");
        Assertions.assertEquals(SanitizeUtils.sanitizeValue("abcdefghi"), "abc***ghi");
        Assertions.assertEquals(SanitizeUtils.sanitizeValue("abcdefghij"), "abc***ghij");
    }
}