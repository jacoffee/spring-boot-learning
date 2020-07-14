package com.jacoffee.learning.elasticsearch.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * JsonUtils
 * @author jacoffee
 * @date 2020-07-05
 */
public class JsonUtils {

    private JsonUtils() {
    }

    private static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    /**
     * 序列化成String
     * @param value value
     * @return
     */
    public static String writeValueAsString(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            // just ignore
            return null;
        }
    }

    /**
     * 反解
     * @param json json
     * @param clazz clazz
     * @param <T> t
     * @return
     */
    public static <T> T readValueAs(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            // just ignore
            return null;
        }
    }

}
