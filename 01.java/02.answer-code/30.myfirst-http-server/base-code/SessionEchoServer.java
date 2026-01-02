import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 세션 유지형 Echo 서버
 * - 클라이언트와 연결을 유지하면서 여러 메시지를 주고받음
 * - WebSocket, SSE와 유사한 방식으로 연결 지속
 * - 클라이언트가 "quit"를 보내거나 연결을 끊을 때까지 계속 통신
 */
public class SessionEchoServer {
    public static void main(String[] args) {
        int port = 8080;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Session Echo Server started on port " + port);
            System.out.println("Waiting for clients...");

            while (true) {
                // 클라이언트 접속 대기
                Socket clientSocket = serverSocket.accept();
                System.out.println("\n=== New Client Connected ===");
                System.out.println("Client: " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
                System.out.println("Server: " + clientSocket.getLocalAddress() + ":" + clientSocket.getLocalPort());

                // 각 클라이언트를 별도 스레드에서 처리 (다중 클라이언트 지원)
                Thread clientHandler = new Thread(new ClientHandler(clientSocket));
                // 클라이언트 핸들러 스레드 시작 후 즉시 메인 스레드로 복귀
                clientHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 개별 클라이언트 세션 처리 담당 스레드 클래스
     * 
     * 역할:
     * - 각 클라이언트마다 독립적인 스레드에서 실행됨
     * - 하나의 클라이언트와의 전체 통신 생명주기를 관리 (연결 → 메시지 송수신 → 종료)
     * - 블로킹 I/O 작업(readLine())을 수행하므로 별도 스레드에서 실행 필요
     * 
     * 통신 흐름:
     * 1. 환영 메시지 전송
     * 2. 클라이언트 메시지 대기 (readLine() 블로킹)
     * 3. "quit" 확인 또는 echo 응답
     * 4. 종료 시 리소스 정리
     */
    static class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            String clientInfo = clientSocket.getInetAddress() + ":" + clientSocket.getPort();
            
            try (
                // 클라이언트로부터 데이터를 읽기 위한 입력 스트림 생성
                // InputStream -> InputStreamReader -> BufferedReader 순서로 래핑
                // (데이터를 한 줄씩 읽기 위해 BufferedReader 사용)
                BufferedReader reader = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream())
                );
                // 클라이언트에게 데이터를 보내기 위한 출력 스트림 생성
                // OutputStream -> PrintWriter로 래핑 (문자열 전송 용이)
                // autoFlush=true로 설정하여 println() 호출 시 자동으로 버퍼 플러시
                PrintWriter writer = new PrintWriter(
                    clientSocket.getOutputStream(), true
                )
            ) {
                // 환영 메시지 전송
                writer.println("=== Welcome to Session Echo Server ===");
                writer.println("Type 'quit' to disconnect");
                writer.println("---");

                // 세션 유지: 클라이언트로부터 계속 메시지 수신
                String line;
                int messageCount = 0;
                
                while ((line = reader.readLine()) != null) {
                    messageCount++;
                    System.out.println("[" + clientInfo + "] Received (#" + messageCount + "): " + line);

                    // 종료 명령 확인
                    if ("quit".equalsIgnoreCase(line.trim())) {
                        writer.println("Goodbye! Closing connection...");
                        System.out.println("[" + clientInfo + "] Client requested disconnect");
                        break;
                    }

                    // Echo 응답 전송
                    String response = "[Echo #" + messageCount + "] " + line;
                    writer.println(response);
                    
                    // 추가 정보 출력
                    if (messageCount % 5 == 0) {
                        writer.println("--- You've sent " + messageCount + " messages so far ---");
                    }
                }

                // 세션 종료 로그 (정상 종료 경우)
                System.out.println("[" + clientInfo + "] Session ended. Total messages: " + messageCount);

            } catch (IOException e) {
                // I/O 예외 처리 (네트워크 오류, 클라이언트 강제 종료 등)
                System.out.println("[" + clientInfo + "] Connection error: " + e.getMessage());
            } finally {
                // ========== 리소스 정리 단계 ==========
                // 클라이언트 연결 종료 및 소켓 리소스 해제
                // (try-with-resources의 reader, writer는 자동 닫힘)
                try {
                    clientSocket.close();
                    System.out.println("[" + clientInfo + "] Socket closed");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
