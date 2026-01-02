import java.time.Year;
import java.util.Scanner;

public class AgeCalc {
    public static void main(String[] args) throws Exception {
        System.out.println("나이 계산기에 오신것을 환영합니다");
        
        // Scanner 객체 생성
        Scanner scanner = new Scanner(System.in);

        // 사용자 입력 받기
        System.out.print("이름을 입력하세요: ");
        String name = scanner.nextLine();

        System.out.print("출생 연도를 입력하세요 (예: 2000): ");
        int birthYear = scanner.nextInt();

        // 현재 연도 가져오기
        int currentYear = Year.now().getValue();

        // 만 나이 계산 (현재 연도 - 출생 연도)
        int age = currentYear - birthYear;

        // 결과 출력
        System.out.println(name + "님의 만 나이는 " + age + "세입니다.");

        // Scanner 닫기
        scanner.close();
    }
}
