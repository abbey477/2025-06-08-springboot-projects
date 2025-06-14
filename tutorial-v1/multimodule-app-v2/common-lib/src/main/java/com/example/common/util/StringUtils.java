package com.example.common.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StringUtils {

    public static boolean isNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }

    public static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    public static String sanitize(String str) {
        if (str == null) {
            return null;
        }
        return str.trim().replaceAll("[<>\"'&]", "");
    }
}