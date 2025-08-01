package com.example.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonUtils {
    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    public static String toJson(Object object) {
        try {
            String json = objectMapper.writeValueAsString(object);
            logger.debug("Converted object to JSON: {}", json);
            return json;
        } catch (JsonProcessingException e) {
            logger.error("Error converting object to JSON", e);
            throw new RuntimeException("Failed to convert to JSON", e);
        }
    }
    
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            T object = objectMapper.readValue(json, clazz);
            logger.debug("Converted JSON to object of type: {}", clazz.getSimpleName());
            return object;
        } catch (JsonProcessingException e) {
            logger.error("Error converting JSON to object", e);
            throw new RuntimeException("Failed to parse JSON", e);
        }
    }
}
