package com.skala.aopanotation.service;

import com.skala.aopanotation.annotation.Hint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Hint 어노테이션을 사용하는 서비스
 */
@Slf4j
@Service
public class HintService {

    /**
     * 단순한 처리 - Level 1
     */
    @Hint(value = "Simple hint", level = 1)
    public String simpleProcess(String input) {
        log.info("SimpleProcess 실행 중... Input: {}", input);
        return "Processed: " + input;
    }

    /**
     * 중복 처리 - Level 2
     */
    @Hint(value = "Medium hint", level = 2)
    public String mediumProcess(String input, int count) {
        log.info("MediumProcess 실행 중... Input: {}, Count: {}", input, count);
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < count; i++) {
            result.append(input).append(" ");
        }
        return result.toString().trim();
    }

    /**
     * 복잡한 처리 - Level 3
     */
    @Hint(value = "Complex hint - This is important!", level = 3)
    public String complexProcess(String input) {
        log.info("ComplexProcess 실행 중... Input: {}", input);
        try {
            Thread.sleep(1000); // 복잡한 처리 시뮬레이션
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return "Complex result: " + input.toUpperCase();
    }

    /**
     * @Hint가 없는 일반 메서드 (AOP 미적용)
     */
    public String normalProcess(String input) {
        log.info("NormalProcess 실행 중... Input: {}", input);
        return "Normal: " + input;
    }
}
