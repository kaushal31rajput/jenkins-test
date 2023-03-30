package com.hcl.icontrol.jenkins

import org.apache.commons.lang.RandomStringUtils

class TextUtils {
    static String IMAGE_TAG_SANITIZE_REGEX = "[^A-Za-z0-9_.]"
    static String CACHE_KEY_SANITIZE_REGEX = "[^A-Za-z0-9_-]"

    private TextUtils() {}

    static String randomStringPrefixed(String prefix, int length) {
        return prefix + RandomStringUtils.randomAlphanumeric(length).toLowerCase()
    }

    static sanitizeImageName(String name) {
        return name.replaceAll(IMAGE_TAG_SANITIZE_REGEX, "-")
    }

    static sanitizeCacheKey(String name) {
        return name.replaceAll(CACHE_KEY_SANITIZE_REGEX, "-")
    }
}
