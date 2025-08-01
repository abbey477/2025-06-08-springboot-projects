package com.example.common;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ValidationUtils {
    private static final Logger logger = LoggerFactory.getLogger(ValidationUtils.class);
    
    public static boolean isValidEmail(String email) {
        if (Strings.isNullOrEmpty(email)) {
            return false;
        }
        // Simple email validation
        boolean isValid = email.contains("@") && email.contains(".");
        logger.debug("Email validation for '{}': {}", email, isValid);
        return isValid;
    }
    
    public static boolean isNumeric(String str) {
        if (Strings.isNullOrEmpty(str)) {
            return false;
        }
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    public static void requireNonNull(Object obj, String message) {
        if (obj == null) {
            logger.error("Validation failed: {}", message);
            throw new IllegalArgumentException(message);
        }
    }
}
