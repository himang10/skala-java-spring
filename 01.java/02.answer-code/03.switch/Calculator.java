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
        double result = switch (operator) {
            case "+" -> firstNumber + secondNumber;
            case "-" -> firstNumber - secondNumber;
            case "*" -> firstNumber * secondNumber;
            case "/" -> (double) firstNumber / secondNumber;
            default -> {
                System.out.println("잘못된 연산자입니다.");
                scanner.close();
                yield 0;
            }
        };
        
        // 결과 출력 (잘못된 연산자인 경우는 이미 처리되었으므로 default가 아닌 경우만)
        if (operator.equals("+") || 
            operator.equals("-") || 
            operator.equals("*") || 
            operator.equals("/")) {
            // 나눗셈의 경우 항상 실수로 표시, 나머지는 정수로 표시
            System.out.println("결과: " + result);

        }
        
        scanner.close();
    }
}