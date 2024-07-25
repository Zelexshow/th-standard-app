package com.bytepound.thstandapp.server.utils;

import com.bytepound.thstandapp.server.model.constant.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConvertUtils {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static <T> T strToObject(String value, TypeReference<T> typeReference) {
        try {
            return OBJECT_MAPPER.readValue(value, typeReference);
        } catch (JsonProcessingException e) {
            log.error("请求参数:{}", value);
            log.error("json转object异常,", e);
            throw ErrorCode.SERVER_ERROR.buildBizException("json转object异常");
        }
    }

    public static String objectToStr(Object object) {
        try {
            OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("请求参数:{}", object);
            log.error("json转object异常,", e);
            throw ErrorCode.SERVER_ERROR.buildBizException("Object转json异常");
        }
    }
}
