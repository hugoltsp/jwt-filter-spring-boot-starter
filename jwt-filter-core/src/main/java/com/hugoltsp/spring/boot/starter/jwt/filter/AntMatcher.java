package com.hugoltsp.spring.boot.starter.jwt.filter;

import org.springframework.util.AntPathMatcher;

final class AntMatcher {

    private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

    private AntMatcher() {

    }

    static boolean matches(String pattern1, String pattern2) {

        return ANT_PATH_MATCHER.match(pattern1, pattern2);
    }

}
