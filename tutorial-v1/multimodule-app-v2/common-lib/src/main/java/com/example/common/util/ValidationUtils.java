package com.example.common.util;

import lombok.experimental.UtilityClass;
import java.util.regex.Pattern;

@UtilityClass
public class ValidationUtils {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isValidName(String name) {
        return StringUtils.isNotEmpty(name) && name.length() >= 2 && name.length() <= 50;
    }
}