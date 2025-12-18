package com.skala.examples.exception;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 전역적으로 400 Bad Request 예외 처리
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> handleBadRequest(BadRequestException ex) {
        return ResponseEntity.badRequest().body("Global 400 Error: " + ex.getMessage());
    }

    // 전역적으로 500 Internal Server Error 예외 처리
    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<String> handleInternalServerError(RuntimeException ex) {
        return ResponseEntity.internalServerError().body("Global 500 Error: " + ex.getMessage());
    }

    // 기타 모든 예외 처리 (예상하지 못한 예외)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return ResponseEntity.internalServerError().body("Global Error: 알 수 없는 오류가 발생했습니다.");
    }
}
