import java.util.Scanner;

public class Calculator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        boolean isRun = true;
        while (isRun) { // 계속 반복
            // 첫 번째 숫자 입력
            System.out.print("첫 번째 숫자: ");
            int firstNumber = scanner.nextInt();

            // 연산자 입력
            System.out.print("연산자(+ - * /): ");
            String operator = scanner.next().trim();

            // 두 번째 숫자 입력
            System.out.print("두 번째 숫자: ");
            int secondNumber = scanner.nextInt();

            // 0으로 나누기 예외 처리
            if (operator.equals("/") && secondNumber == 0) {
                System.out.println("0으로 나눌 수 없습니다.");
            } else {
                // switch expression으로 계산
                double result = switch (operator) {
                    case "+" -> firstNumber + secondNumber;
                    case "-" -> firstNumber - secondNumber;
                    case "*" -> firstNumber * secondNumber;
                    case "/" -> (double) firstNumber / secondNumber;
                    default -> {
                        System.out.println("잘못된 연산자입니다.");
                        yield Double.NaN; // 잘못된 경우 NaN 반환
                    }
                };

                // 유효한 연산자일 경우 결과 출력
                if (!Double.isNaN(result)) {
                    System.out.println("결과: " + result);
                }
            }

            // 계속 여부 입력
            System.out.print("계속하려면 c(continue) / 종료하려면 q(quit) 입력: ");
            String choice = scanner.next().trim().toLowerCase();

            if (choice.equals("q") || choice.equals("quit")) {
                isRun = false;
            } 
        }

        scanner.close();
    }
}
