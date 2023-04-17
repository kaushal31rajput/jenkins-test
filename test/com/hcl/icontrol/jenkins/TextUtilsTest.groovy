package com.hcl.icontrol.jenkins

import org.apache.commons.lang3.StringUtils
import spock.lang.Specification

class TextUtilsTest extends Specification {

    def "should generate lowercase random string prefixed"() {
        expect:
        TextUtils.randomStringPrefixed("app-", 8).length() == 12
        def value = TextUtils.randomStringPrefixed("app-", 8)
        value == StringUtils.lowerCase(value)
    }

    def "should sanitize non-allowed chars from image tag"() {
        expect:
        TextUtils.sanitizeImageName("DEVELOP-845/new-feature") == "DEVELOP-845-new-feature"
        TextUtils.sanitizeImageName("1.2.3") == "1.2.3"
    }

    def "should sanitize non-allowed chars from cache key"() {
        expect:
        TextUtils.sanitizeCacheKey("DEVELOP-845/new-feature") == "DEVELOP-845-new-feature"
        TextUtils.sanitizeCacheKey("1.2.3") == "1-2-3"
        TextUtils.sanitizeCacheKey("image-utils") == "image-utils"
    }
}
