import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class SimpleEchoClient {
    public static void main(String[] args) {
        String host = "localhost"; // 또는 서버 IP
        int port = 8080;

        try {
            // InetAddress 사용 (선택)
            InetAddress serverAddr = InetAddress.getByName(host);

            try (
                Socket socket = new Socket(serverAddr, port);
                BufferedReader reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
                );
                PrintWriter writer = new PrintWriter(
                    socket.getOutputStream(), true
                )
            ) {
                // 서버로 메시지 전송
                writer.println("Hello Server!");

                // 서버에서 Echo 응답 수신
                String response = reader.readLine();
                System.out.println("Response: " + response);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }    
}
