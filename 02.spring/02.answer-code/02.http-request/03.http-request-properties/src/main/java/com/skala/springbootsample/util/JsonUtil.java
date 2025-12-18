package com.skala.springbootsample.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {
    // 역직렬화 시 객체 클래스에 존재하지 않는 필드가 있어도 무시하도록 설정
    private static final ObjectMapper MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public static ObjectMapper mapper() { return MAPPER; }
}
