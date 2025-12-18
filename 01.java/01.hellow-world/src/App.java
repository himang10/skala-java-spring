import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");

        Scanner scanner = new Scanner(System.in);
        System.out.print("이름을 입력하세요: ");
        String name = scanner.nextLine();
        System.out.println("안녕하세요, " + name + "님!");
        System.out.print("나이를 입력하세요: ");
        int age = scanner.nextInt();
        System.out.println("당신의 나이는 " + age + "세 입니다.");
        scanner.close();
        Thread.sleep(30000);

    }
}
