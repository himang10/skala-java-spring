package com.skala.purejava.http.server;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * HTTP 응답을 생성하고 클라이언트에게 전송하는 유틸리티 클래스
 *
 * HTTP 응답의 구조:
 * HTTP/1.1 200 OK
 * Content-Type: application/json; charset=utf-8
 * Content-Length: 45
 * Connection: close
 *
 * {"id":1,"name":"홍길동","email":"hong@example.com"}
 *
 * 응답 구성 요소:
 * 1. 상태 라인: HTTP 버전, 상태 코드, 상태 메시지
 * 2. 헤더들: Content-Type, Content-Length 등
 * 3. 빈 줄: 헤더와 본문 구분
 * 4. 응답 본문: 실제 데이터 (JSON, HTML, 텍스트 등)
 */
public class HttpResponse {

    /**
     * JSON 형태의 HTTP 응답을 생성하여 클라이언트에게 전송
     *
     * 사용 예시:
     * - API 응답: {"users": [...]}
     * - 에러 응답: {"error": "사용자를 찾을 수 없습니다"}
     * - 성공 응답: {"message": "저장되었습니다"}
     *
     * @param out    클라이언트 소켓의 OutputStream (응답을 보낼 대상)
     * @param status HTTP 상태 코드 (200=성공, 404=찾을수없음, 500=서버오류 등)
     * @param json   응답할 JSON 문자열
     * @throws IOException 네트워크 전송 실패시
     */
    public static void writeJson(OutputStream out, int status, String json) throws IOException {
        // JSON 문자열을 UTF-8 바이트 배열로 변환
        // 한글, 특수문자 등을 올바르게 전송하기 위해 UTF-8 사용
        byte[] body = json.getBytes(StandardCharsets.UTF_8);

        // HTTP 헤더 작성 및 전송
        // Content-Type을 application/json으로 설정하여 클라이언트가 JSON임을 알 수 있게 함
        writeHeaders(out, status, "application/json; charset=utf-8", body.length);

        // 실제 JSON 데이터 전송
        out.write(body);

        // 버퍼에 있는 데이터를 즉시 전송 (네트워크로 실제 전송)
        out.flush();
    }

    /**
     * 텍스트 형태의 HTTP 응답을 생성하여 클라이언트에게 전송
     *
     * 사용 예시:
     * - 간단한 메시지: "OK"
     * - 에러 메시지: "잘못된 요청입니다"
     * - 상태 응답: "삭제되었습니다"
     *
     * @param out    클라이언트 소켓의 OutputStream
     * @param status HTTP 상태 코드
     * @param text   응답할 텍스트 문자열
     * @throws IOException 네트워크 전송 실패시
     */
    public static void writeText(OutputStream out, int status, String text) throws IOException {
        // 텍스트를 UTF-8 바이트 배열로 변환
        byte[] body = text.getBytes(StandardCharsets.UTF_8);

        // HTTP 헤더 작성 및 전송
        // Content-Type을 text/plain으로 설정하여 클라이언트가 일반 텍스트임을 알 수 있게 함
        writeHeaders(out, status, "text/plain; charset=utf-8", body.length);

        // 실제 텍스트 데이터 전송
        out.write(body);

        // 즉시 전송
        out.flush();
    }

    /**
     * HTTP 응답 헤더를 작성하여 전송
     *
     * 생성되는 헤더 예시:
     * HTTP/1.1 200 OK
     * Content-Type: application/json; charset=utf-8
     * Content-Length: 123
     * Connection: close
     * (빈 줄)
     *
     * @param out         클라이언트 소켓의 OutputStream
     * @param status      HTTP 상태 코드 (200, 404, 500 등)
     * @param contentType 콘텐츠 타입 (application/json, text/plain 등)
     * @param length      응답 본문의 바이트 길이
     * @throws IOException 네트워크 전송 실패시
     */
    private static void writeHeaders(OutputStream out, int status, String contentType, int length) throws IOException {

        // HTTP 상태 코드를 사람이 읽을 수 있는 메시지로 변환
        // Java 14의 switch 표현식 사용 (간결하고 안전한 방식)
        String statusText = switch (status) {
            case 200 -> "OK";                    // 성공
            case 201 -> "Created";               // 리소스 생성 성공
            case 204 -> "No Content";            // 성공이지만 응답 본문 없음 (DELETE 요청 등)
            case 400 -> "Bad Request";           // 잘못된 요청
            case 404 -> "Not Found";             // 리소스 없음
            case 405 -> "Method Not Allowed";    // 허용되지 않은 HTTP 메서드
            case 500 -> "Internal Server Error"; // 서버 내부 오류
            default -> "OK";                     // 정의되지 않은 상태 코드는 OK로 처리
        };

        // HTTP 응답 헤더 문자열 구성
        // \r\n: HTTP 표준에서 정의한 줄바꿈 문자 (Carriage Return + Line Feed)
        String headers =
                "HTTP/1.1 " + status + " " + statusText + "\r\n" +     // 상태 라인
                        "Content-Type: " + contentType + "\r\n" +              // 콘텐츠 타입
                        "Content-Length: " + length + "\r\n" +                 // 본문 길이 (바이트)
                        "Connection: close\r\n" +                              // 연결 종료 (Keep-Alive 미사용)
                        "\r\n";                                                // 헤더 끝을 나타내는 빈 줄

        // 헤더 문자열을 UTF-8 바이트로 변환하여 전송
        out.write(headers.getBytes(StandardCharsets.UTF_8));
    }
}
