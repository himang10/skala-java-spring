package com.skala.purejava.http.server;

import java.io.*;
import java.net.*;
import java.util.Optional;
import java.util.concurrent.*;

import com.skala.purejava.http.server.Router.Matched;

/**
 * 간단한 HTTP 서버 구현체
 * - Socket 기반으로 HTTP 요청을 받아서 처리
 * - 멀티스레드로 동시에 여러 클라이언트 요청 처리 가능
 * - Router를 통해 URL 경로별로 다른 핸들러 실행
 */
public class HttpServerLite {

    private final int port;                    // 서버가 listen할 포트 번호
    private final Router router;               // URL 라우팅을 담당하는 라우터
    private final ExecutorService threadPool;  // 클라이언트 요청 처리용 스레드풀

    /**
     * HTTP 서버 생성자
     * @param port 서버 포트 (예: 8080)
     * @param router URL 라우팅 담당 객체
     * @param threads 동시 처리할 수 있는 최대 스레드 수
     */
    public HttpServerLite(int port, Router router, int threads) {
        this.port = port;
        this.router = router;
        this.threadPool = Executors.newFixedThreadPool(threads);
    }

    /**
     * 서버를 시작하고 클라이언트 연결을 대기합니다
     * 이 메서드는 블로킹되어 계속 실행됩니다
     */
    public void start() throws IOException {
        // ServerSocket: 클라이언트의 연결 요청을 받는 소켓
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("서버가 포트 " + port + "에서 시작되었습니다.");

            // 무한 루프로 클라이언트 연결 대기
            while (true) {
                // accept(): 클라이언트 연결이 올 때까지 대기하다가 연결되면 Socket 반환
                Socket clientSocket = serverSocket.accept();
                System.out.println("클라이언트 연결: " + clientSocket.getInetAddress());

                // 각 클라이언트 요청을 별도 스레드에서 처리
                // Thread Pool에  clientSocket을 BlockQueue에 clientSocket을 추가
                threadPool.submit(() -> handleClient(clientSocket));
            }
        }
    }

    /**
     * 개별 클라이언트의 HTTP 요청을 처리합니다
     * @param clientSocket 클라이언트와 연결된 소켓
     */
    private void handleClient(Socket clientSocket) {
        // try-with-resources: 자동으로 소켓과 스트림을 닫아줌
        try (clientSocket;
             InputStream input = clientSocket.getInputStream();    // 클라이언트로부터 데이터 읽기
             OutputStream output = clientSocket.getOutputStream()) { // 클라이언트에게 데이터 쓰기

            // 1단계: HTTP 요청 파싱
            // 클라이언트가 보낸 HTTP 요청(헤더, 바디 등)을 HttpRequest 객체로 변환
            HttpRequest request = HttpRequest.parse(input);
            System.out.println("요청 처리: " + request.method + " " + request.path);

            // 2단계: 라우터에서 매칭되는 핸들러 찾기
            // 예: "GET /users/123" 요청이 오면 해당하는 핸들러를 찾음
            Optional<Matched> matchedRoute = router.match(request.method, request.path);

            // 3단계: 매칭되는 라우트가 없으면 404 응답
            if (matchedRoute.isEmpty()) {
                System.out.println("404 Not Found: " + request.method + " " + request.path);
                HttpResponse.writeJson(output, 404, "{\"error\":\"페이지를 찾을 수 없습니다\"}");
                return;
            }

            // 4단계: 경로 변수를 요청에 추가
            // 예: "/users/{id}" 패턴에서 {id} 부분을 추출하여 request.pathVars에 저장
            request.pathVars.putAll(matchedRoute.get().pathVars());

            // 5단계: 실제 비즈니스 로직을 담당하는 핸들러 실행
            try {
                // handle()은 route.add 를 통해 등록한 자신의 RouteHandler를 여기서 호출
                matchedRoute.get().handler().handle(request, output);
                System.out.println("요청 처리 완료: " + request.method + " " + request.path);
            } catch (Exception e) {
                // 핸들러 실행 중 오류 발생시 500 에러 응답
                System.err.println("핸들러 실행 중 오류: " + e.getMessage());
                e.printStackTrace();
                HttpResponse.writeJson(output, 500, "{\"error\":\"서버 내부 오류가 발생했습니다\"}");
            }

        } catch (SocketException e) {
            // 소켓 관련 예외 (연결이 끊어진 경우 등) - 정상적인 상황
            if (e.getMessage().contains("Connection reset") ||
                    e.getMessage().contains("Broken pipe") ||
                    e.getMessage().contains("Socket closed")) {
                System.out.println("클라이언트 연결 종료 (정상): " + e.getMessage());
            } else {
                System.err.println("소켓 오류: " + e.getMessage());
            }
        } catch (IOException e) {
            // 네트워크 I/O 오류
            if (e.getMessage().contains("Connection reset") ||
                    e.getMessage().contains("Broken pipe")) {
                System.out.println("클라이언트 연결 종료 (정상): " + e.getMessage());
            }
        } catch (Exception e) {
            // HTTP 요청 파싱이나 기타 예외
            System.err.println("클라이언트 처리 중 예상치 못한 오류: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            e.printStackTrace();
        }
    }
}
