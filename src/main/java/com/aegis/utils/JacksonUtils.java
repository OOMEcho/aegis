package com.aegis.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.*;

/**
 * @Author: xuesong.lei
 * @Date: 2025/08/20 09:28
 * @Description: Jackson JSON 工具类
 */
@Slf4j
public final class JacksonUtils {

    private static final ObjectMapper MAPPER;
    private static final ObjectMapper PRETTY_MAPPER;

    static {
        MAPPER = createMapper();
        PRETTY_MAPPER = createPrettyMapper();
    }

    private JacksonUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * 创建标准ObjectMapper
     */
    private static ObjectMapper createMapper() {
        ObjectMapper mapper = new ObjectMapper();

        // 序列化配置
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        // 反序列化配置
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);

        return mapper;
    }

    /**
     * 创建格式化ObjectMapper
     */
    private static ObjectMapper createPrettyMapper() {
        ObjectMapper mapper = createMapper();
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        return mapper;
    }

    /**
     * 对象转JSON字符串
     */
    public static String toJson(Object obj) {
        if (obj == null) {
            return null;
        }

        try {
            return MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("序列化失败: {}", e.getMessage(), e);
            throw new RuntimeException("JSON序列化失败", e);
        }
    }

    /**
     * 对象转格式化JSON字符串
     */
    public static String toPrettyJson(Object obj) {
        if (obj == null) {
            return null;
        }

        try {
            return PRETTY_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("格式化序列化失败: {}", e.getMessage(), e);
            throw new RuntimeException("JSON格式化序列化失败", e);
        }
    }

    /**
     * JSON字符串转对象
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        if (!StringUtils.hasText(json)) {
            return null;
        }

        try {
            return MAPPER.readValue(json, clazz);
        } catch (IOException e) {
            log.error("反序列化失败: {}, JSON: {}", e.getMessage(), json, e);
            throw new RuntimeException("JSON反序列化失败", e);
        }
    }

    /**
     * JSON字符串转对象，使用TypeReference
     */
    public static <T> T fromJson(String json, TypeReference<T> typeRef) {
        if (!StringUtils.hasText(json)) {
            return null;
        }

        try {
            return MAPPER.readValue(json, typeRef);
        } catch (IOException e) {
            log.error("TypeReference反序列化失败: {}, JSON: {}", e.getMessage(), json, e);
            throw new RuntimeException("JSON反序列化失败", e);
        }
    }

    /**
     * JSON转List
     */
    public static <T> List<T> fromJsonToList(String json, Class<T> clazz) {
        if (!StringUtils.hasText(json)) {
            return new ArrayList<>();
        }

        try {
            JavaType listType = MAPPER.getTypeFactory().constructParametricType(List.class, clazz);
            return MAPPER.readValue(json, listType);
        } catch (IOException e) {
            log.error("List反序列化失败: {}, JSON: {}", e.getMessage(), json, e);
            throw new RuntimeException("JSON转List失败", e);
        }
    }

    /**
     * JSON转Map
     */
    public static Map<String, Object> fromJsonToMap(String json) {
        return fromJson(json, new TypeReference<Map<String, Object>>() {
        });
    }

    /**
     * JSON转指定类型Map
     */
    public static <K, V> Map<K, V> fromJsonToMap(String json, Class<K> keyClass, Class<V> valueClass) {
        if (!StringUtils.hasText(json)) {
            return new HashMap<>();
        }

        try {
            JavaType mapType = MAPPER.getTypeFactory().constructParametricType(Map.class, keyClass, valueClass);
            return MAPPER.readValue(json, mapType);
        } catch (IOException e) {
            log.error("Map反序列化失败: {}, JSON: {}", e.getMessage(), json, e);
            throw new RuntimeException("JSON转Map失败", e);
        }
    }

    /**
     * 对象转换
     */
    public static <T> T convert(Object obj, Class<T> clazz) {
        if (obj == null) {
            return null;
        }

        try {
            return MAPPER.convertValue(obj, clazz);
        } catch (IllegalArgumentException e) {
            log.error("对象转换失败: {}", e.getMessage(), e);
            throw new RuntimeException("对象转换失败", e);
        }
    }

    /**
     * 对象转换，使用TypeReference
     */
    public static <T> T convert(Object obj, TypeReference<T> typeRef) {
        if (obj == null) {
            return null;
        }

        try {
            return MAPPER.convertValue(obj, typeRef);
        } catch (IllegalArgumentException e) {
            log.error("TypeReference转换失败: {}", e.getMessage(), e);
            throw new RuntimeException("对象转换失败", e);
        }
    }

    /**
     * 字符串转JsonNode
     */
    public static JsonNode parseNode(String json) {
        if (!StringUtils.hasText(json)) {
            return null;
        }

        try {
            return MAPPER.readTree(json);
        } catch (IOException e) {
            log.error("JsonNode解析失败: {}, JSON: {}", e.getMessage(), json, e);
            throw new RuntimeException("JsonNode解析失败", e);
        }
    }

    /**
     * 创建ObjectNode
     */
    public static ObjectNode newObject() {
        return MAPPER.createObjectNode();
    }

    /**
     * 创建ArrayNode
     */
    public static ArrayNode newArray() {
        return MAPPER.createArrayNode();
    }

    /**
     * 检查是否为有效JSON
     */
    public static boolean isValidJson(String json) {
        if (!StringUtils.hasText(json)) {
            return false;
        }

        try {
            MAPPER.readTree(json);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 安全解析，失败时返回默认值
     */
    public static <T> T parseOrDefault(String json, Class<T> clazz, T defaultValue) {
        try {
            T result = fromJson(json, clazz);
            return result != null ? result : defaultValue;
        } catch (Exception e) {
            log.warn("安全解析失败，返回默认值: {}", e.getMessage());
            return defaultValue;
        }
    }

    /**
     * 深拷贝对象
     */
    public static <T> T deepCopy(T original, Class<T> clazz) {
        if (original == null) {
            return null;
        }

        try {
            String json = MAPPER.writeValueAsString(original);
            return MAPPER.readValue(json, clazz);
        } catch (IOException e) {
            log.error("深拷贝失败: {}", e.getMessage(), e);
            throw new RuntimeException("深拷贝失败", e);
        }
    }

    /**
     * 获取ObjectMapper实例
     */
    public static ObjectMapper getMapper() {
        return MAPPER;
    }

    /**
     * 合并两个ObjectNode
     */
    public static ObjectNode merge(ObjectNode main, ObjectNode update) {
        if (update == null) {
            return main;
        }
        if (main == null) {
            return update.deepCopy();
        }

        update.fields().forEachRemaining(entry ->
                main.set(entry.getKey(), entry.getValue())
        );

        return main;
    }

    /**
     * 提取对象中的指定字段值
     */
    public static Object getFieldValue(Object obj, String fieldPath) {
        try {
            JsonNode node = MAPPER.valueToTree(obj);
            String[] paths = fieldPath.split("\\.");

            for (String path : paths) {
                if (node == null || node.isNull()) {
                    return null;
                }
                node = node.get(path);
            }

            return node != null && !node.isNull() ? node.asText() : null;
        } catch (Exception e) {
            log.warn("字段值提取失败: {}", e.getMessage());
            return null;
        }
    }
}
