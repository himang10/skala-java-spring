package com.skala.purejava.http.server;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * HTTP 요청 정보를 파싱하고 저장하는 클래스
 *
 * HTTP 요청의 구조:
 * GET /users/123?name=홍길동 HTTP/1.1
 * Host: localhost:8080
 * Content-Type: application/json
 * Content-Length: 45
 *
 * {"name":"홍길동","email":"hong@example.com"}
 */
public class HttpRequest {

    // HTTP 메서드 (GET, POST, PUT, DELETE 등)
    public String method;

    // 요청 경로 (/users, /users/123)
    public String path;

    // 쿼리 스트링 (name=홍길동&age=30)
    public String queryString;

    // 쿼리 파라미터를 key-value로 파싱한 결과 (name -> 홍길동, age -> 30)
    public Map<String,String> query = new HashMap<>();

    // HTTP 헤더들 (content-type -> application/json, host -> localhost:8080)
    public Map<String,String> headers = new HashMap<>();

    // HTTP 요청 본문 (JSON 데이터 등)
    public String body = "";

    // URL 경로 변수 (예: /users/{id}에서 {id} 부분) - Router가 나중에 설정
    public Map<String,String> pathVars = new HashMap<>();

    /**
     * 경로 변수를 Long 타입으로 변환하여 반환
     * 예: /users/{id}에서 id 값을 Long으로 변환
     *
     * @param name 경로 변수 이름 (예: "id")
     * @return Long 값 또는 null (변환 실패시)
     */
    public Long pathLong(String name) {
        String v = pathVars.get(name);
        if (v == null) return null;
        try {
            return Long.parseLong(v);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * InputStream에서 HTTP 요청을 파싱하여 HttpRequest 객체로 변환
     *
     * HTTP 요청 파싱 과정:
     * 1. 첫 번째 줄: 메서드, 경로, HTTP 버전 파싱
     * 2. 헤더들: 각 줄을 key: value 형태로 파싱
     * 3. 빈 줄 이후: 요청 본문 읽기
     *
     * @param in 클라이언트 소켓의 InputStream
     * @return 파싱된 HttpRequest 객체
     * @throws IOException 요청 파싱 실패시
     */
    static HttpRequest parse(InputStream in) throws IOException {
        // UTF-8로 텍스트 읽기 위한 BufferedReader
        BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

        // 1단계: 요청 라인 파싱 (예: "GET /users/123?name=홍길동 HTTP/1.1")
        String requestLine = br.readLine();
        if (requestLine == null || requestLine.isEmpty()) {
            throw new IOException("empty request");
        }

        // 공백으로 분리 [GET, /users/123?name=홍길동, HTTP/1.1]
        String[] parts = requestLine.split(" ");
        if (parts.length < 3) {
            throw new IOException("bad request line");
        }

        HttpRequest req = new HttpRequest();
        req.method = parts[0];  // GET, POST, PUT, DELETE 등

        // 2단계: 경로와 쿼리 스트링 분리
        String fullPath = parts[1];  // /users/123?name=홍길동
        int idx = fullPath.indexOf('?');

        if (idx >= 0) {
            // 쿼리 스트링이 있는 경우
            req.path = fullPath.substring(0, idx);        // /users/123
            req.queryString = fullPath.substring(idx + 1); // name=홍길동
            req.query = parseQuery(req.queryString);        // Map으로 변환
        } else {
            // 쿼리 스트링이 없는 경우
            req.path = fullPath;
            req.queryString = "";
        }

        // 3단계: HTTP 헤더들 파싱
        // 빈 줄이 나올 때까지 각 줄을 "key: value" 형태로 파싱
        String line;
        while ((line = br.readLine()) != null && !line.isEmpty()) {
            int p = line.indexOf(':');
            if (p > 0) {
                String k = line.substring(0, p).trim().toLowerCase(Locale.ROOT);  // 헤더명 (소문자)
                String v = line.substring(p + 1).trim();                          // 헤더값
                req.headers.put(k, v);
            }
        }

        // 4단계: 요청 본문 읽기 (POST, PUT 요청에서 JSON 데이터 등)
        int contentLength = 0;
        if (req.headers.containsKey("content-length")) {
            try {
                contentLength = Integer.parseInt(req.headers.get("content-length"));
            } catch (Exception ignore) {
                // Content-Length 파싱 실패시 0으로 처리
            }
        }

        // Content-Length만큼 본문 데이터 읽기
        if (contentLength > 0) {
            char[] buf = new char[contentLength];
            int read = br.read(buf);
            if (read > 0) {
                req.body = new String(buf, 0, read);
            }
        }

        return req;
    }

    /**
     * 쿼리 스트링을 파싱하여 Map으로 변환
     *
     * 예: "name=홍길동&age=30&hobby=독서&hobby=영화"
     * → {name: "홍길동", age: "30", hobby: "영화"} (마지막 값으로 덮어씀)
     *
     * @param q 쿼리 스트링 (예: "name=홍길동&age=30")
     * @return 파싱된 key-value Map
     */
    static Map<String,String> parseQuery(String q) {
        Map<String,String> map = new HashMap<>();
        if (q == null || q.isEmpty()) return map;

        // "&"로 각 파라미터 분리
        for (String pair : q.split("&")) {
            if (pair.isEmpty()) continue;

            // "="로 key-value 분리 (최대 2개로 분리)
            String[] kv = pair.split("=", 2);
            String k = urlDecode(kv[0]);                    // key URL 디코딩
            String v = kv.length > 1 ? urlDecode(kv[1]) : ""; // value URL 디코딩 (없으면 빈 문자열)
            map.put(k, v);
        }
        return map;
    }

    /**
     * URL 인코딩된 문자열을 디코딩
     *
     * 예: "%ED%99%8D%EA%B8%B8%EB%8F%99" → "홍길동"
     *     "hello%20world" → "hello world"
     *
     * @param s URL 인코딩된 문자열
     * @return 디코딩된 문자열 (실패시 원본 반환)
     */
    static String urlDecode(String s) {
        try {
            return URLDecoder.decode(s, StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            // 디코딩 실패시 원본 문자열 반환
            return s;
        }
    }
}
