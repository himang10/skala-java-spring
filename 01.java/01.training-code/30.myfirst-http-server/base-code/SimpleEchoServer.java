import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleEchoServer {
    public static void main(String[] args) {
        int port = 8080;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Echo Server started on port " + port);

            //while (true) {
                // 클라이언트 접속 대기
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                // 간단히 한 번만 Echo 처리 후 종료하는 버전
                try (
                    BufferedReader reader = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream())
                    );
                    PrintWriter writer = new PrintWriter(
                        clientSocket.getOutputStream(), true
                    )
                ) {
                    System.out.println("Waiting for message...");
                    String line = reader.readLine();
                    System.out.println("Received: " + line);

                    writer.println("Echo: " + line);
                }

                clientSocket.close();
            //}
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
