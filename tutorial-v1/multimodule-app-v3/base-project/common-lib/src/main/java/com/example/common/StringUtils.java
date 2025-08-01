package com.example.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.stream.Collectors;

public class StringUtils {
    private static final Logger logger = LoggerFactory.getLogger(StringUtils.class);
    
    public static String capitalize(String input) {
        logger.debug("Capitalizing string: {}", input);
        return StringUtils.capitalize(input);
    }
    
    public static boolean isEmpty(String input) {
        return StringUtils.isEmpty(input);
    }
    
    public static String reverse(String input) {
        logger.debug("Reversing string: {}", input);
        return StringUtils.reverse(input);
    }
    
    public static String joinWithComma(List<String> items) {
        logger.debug("Joining {} items with comma", items.size());
        return String.join(", ", items);
    }
    
    public static List<String> splitAndTrim(String input, String delimiter) {
        if (isEmpty(input)) {
            return List.of();
        }
        return List.of(input.split(delimiter))
                .stream()
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }
}
