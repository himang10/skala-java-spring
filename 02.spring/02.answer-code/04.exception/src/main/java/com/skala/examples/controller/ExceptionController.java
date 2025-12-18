package com.skala.examples.controller;

import com.skala.examples.exception.BadRequestException;
import com.skala.examples.exception.InternalServerException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/exception")
public class ExceptionController {

    // 400 예외 발생
    @GetMapping("/400")
    public void throwBadRequest() {
        throw new BadRequestException("잘못된 요청입니다.");
    }

    // 500 예외 발생
    @GetMapping("/500")
    public void throwInternalServerError() {
        throw new InternalServerException("서버 내부 오류가 발생했습니다.");
    }

    // 전역 예외 처리기로 전달되는 예외 발생 (ControllerAdvice에서 처리)
    @GetMapping("/global")
    public void throwGlobalException() {
        throw new RuntimeException("이 예외는 전역 예외 처리기로 전달됩니다.");
    }

    // 컨트롤러 내부에서 400 Bad Request 예외 처리
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> handleBadRequest(BadRequestException ex) {
        return ResponseEntity.badRequest().body("Controller-Level 400 Error: " + ex.getMessage());
        //return ResponseEntity.status(400).body("Controller-Level 400 Error: " + ex.getMessage());
    }

    // 컨트롤러 내부에서 500 Internal Server Error 예외 처리
    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<String> handleInternalServerError(InternalServerException ex) {
        //return ResponseEntity.internalServerError().body("Controller-Level 500 Error: " + ex.getMessage());
        return ResponseEntity.status(500).body("Controller-Level 500 Error: " + ex.getMessage());
    }

}

