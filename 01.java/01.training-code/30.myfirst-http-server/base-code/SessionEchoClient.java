import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * 세션 유지형 Echo 클라이언트
 * - SessionEchoServer와 연결을 유지하면서 여러 메시지를 주고받음
 * - 사용자로부터 입력을 받아 서버로 전송하고 응답을 출력
 * - "quit" 입력 시 연결 종료
 */
public class SessionEchoClient {
    public static void main(String[] args) {
        String host = "localhost";  // 서버 주소
        int port = 8080;            // 서버 포트

        System.out.println("=== Session Echo Client ===");
        System.out.println("Connecting to " + host + ":" + port + "...");

        // 서버에 연결
        try (
            // 서버와의 소켓 연결 생성
            Socket socket = new Socket(host, port);
            // 서버로부터 데이터를 읽기 위한 입력 스트림
            BufferedReader serverReader = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
            );
            // 서버에게 데이터를 보내기 위한 출력 스트림
            PrintWriter serverWriter = new PrintWriter(
                socket.getOutputStream(), true  // autoFlush=true
            );
            // 사용자로부터 입력을 받기 위한 스캐너
            Scanner userInput = new Scanner(System.in)
        ) {
            System.out.println("Connected to server!");
            System.out.println("Local: " + socket.getLocalAddress() + ":" + socket.getLocalPort());
            System.out.println("Remote: " + socket.getInetAddress() + ":" + socket.getPort());
            System.out.println();

            // 서버의 환영 메시지를 수신하는 스레드 시작
            // (서버 메시지 수신과 사용자 입력을 동시에 처리하기 위해)
            Thread serverMessageListener = new Thread(new ServerMessageHandler(serverReader));
            serverMessageListener.setDaemon(true);  // 메인 스레드 종료 시 함께 종료
            serverMessageListener.start();

            // 짧은 대기 시간을 두어 서버의 환영 메시지가 먼저 출력되도록 함
            Thread.sleep(500);

            // 사용자 입력 처리 루프
            System.out.println("You can start typing messages (type 'quit' to exit):");
            while (true) {
                System.out.print("> ");
                String message = userInput.nextLine();

                // 서버로 메시지 전송
                serverWriter.println(message);

                // quit 명령 확인
                if ("quit".equalsIgnoreCase(message.trim())) {
                    System.out.println("Closing connection...");
                    // 서버의 goodbye 메시지를 받을 시간을 줌
                    Thread.sleep(500);
                    break;
                }
            }

            System.out.println("Client disconnected.");

        } catch (IOException e) {
            System.err.println("Connection error: " + e.getMessage());
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.err.println("Thread interrupted: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 서버로부터 메시지를 수신하는 핸들러 (별도 스레드에서 실행)
     * 
     * 역할:
     * - 서버가 보내는 모든 메시지를 지속적으로 수신하여 출력
     * - 메인 스레드의 사용자 입력과 독립적으로 동작
     * - 서버 연결이 끊어지거나 오류 발생 시 종료
     */
    static class ServerMessageHandler implements Runnable {
        private BufferedReader reader;

        public ServerMessageHandler(BufferedReader reader) {
            this.reader = reader;
        }

        @Override
        public void run() {
            try {
                String serverMessage;
                while ((serverMessage = reader.readLine()) != null) {
                    // 서버 메시지 출력 (프롬프트와 구분하기 위해 줄바꿈 추가)
                    System.out.println("\n[Server] " + serverMessage);
                    System.out.print("> ");  // 프롬프트 재출력
                }
            } catch (IOException e) {
                // 서버 연결이 끊어진 경우 (정상 종료 시에도 발생 가능)
                System.out.println("\n[Server connection closed]");
            }
        }
    }
}
