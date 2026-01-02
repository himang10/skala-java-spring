import java.util.Scanner;

public class Calculator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // 첫 번째 숫자 입력
        System.out.print("첫 번째 숫자: ");
        Integer firstNumber = scanner.nextInt();
        
        // 연산자 입력
        System.out.print("연산자(+ - * /): ");
        String operator = scanner.next();
        
        // 두 번째 숫자 입력
        System.out.print("두 번째 숫자: ");
        Integer secondNumber = scanner.nextInt();
        
        // 0으로 나누기 예외 처리
        if (operator.equals("/") && secondNumber == 0) {
            System.out.println("0으로 나눌 수 없습니다.");
            scanner.close();
            return;
        }
        
        // switch expression을 사용하여 계산

        
       scanner.close();
    }
}