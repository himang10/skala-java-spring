import java.net.InetAddress;

public class InetAddressExample {
    public static void main(String[] args) throws Exception {
        // 도메인 이름으로 IP 조회
        InetAddress harbor = InetAddress.getByName("amdp-registry.skala-ai.com");
        System.out.println("Host Name : " + harbor.getHostName());
        System.out.println("Host Address : " + harbor.getHostAddress());

        // 로컬 호스트 정보 조회
        InetAddress local = InetAddress.getLocalHost();
        System.out.println("Local Host Name : " + local.getHostName());
        System.out.println("Local IP Address : " + local.getHostAddress());
    }
}