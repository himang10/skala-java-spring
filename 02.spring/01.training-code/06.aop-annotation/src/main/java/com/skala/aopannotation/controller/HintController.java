package com.skala.aopanotation.controller;

import com.skala.aopanotation.service.HintService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Hint 어노테이션을 테스트하는 REST 컨트롤러
 */
@Slf4j
@RestController
@RequestMapping("/api/hint")
@RequiredArgsConstructor
public class HintController {

    private final HintService hintService;

    /**
     * Level 1: 단순한 처리
     * 예시: GET /api/hint/simple?input=hello
     */
    @GetMapping("/simple")
    public String simple(@RequestParam String input) {
        return hintService.simpleProcess(input);
    }

    /**
     * Level 2: 중복 처리
     * 예시: GET /api/hint/medium?input=word&count=3
     */
    @GetMapping("/medium")
    public String medium(@RequestParam String input, @RequestParam(defaultValue = "1") int count) {
        return hintService.mediumProcess(input, count);
    }

    /**
     * Level 3: 복잡한 처리
     * 예시: GET /api/hint/complex?input=spring
     */
    @GetMapping("/complex")
    public String complex(@RequestParam String input) {
        return hintService.complexProcess(input);
    }

    /**
     * @Hint가 없는 메서드 (AOP 미적용)
     * 예시: GET /api/hint/normal?input=test
     */
    @GetMapping("/normal")
    public String normal(@RequestParam String input) {
        return hintService.normalProcess(input);
    }
}
