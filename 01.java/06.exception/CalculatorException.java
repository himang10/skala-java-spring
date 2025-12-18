import java.util.Scanner;

public class CalculatorException {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String[] history = new String[100]; // 최대 100개의 계산 기록 저장
        int historyCount = 0; // 저장된 기록 개수

        boolean isRun = true;
        while (isRun) {
            // 첫 번째 숫자 입력
            System.out.print("첫 번째 숫자: ");
            int firstNumber = scanner.nextInt();

            // 연산자 입력
            System.out.print("연산자(+ - * /): ");
            String operator = scanner.next().trim();

            // 두 번째 숫자 입력
            System.out.print("두 번째 숫자: ");
            int secondNumber = scanner.nextInt();

            String record = ""; // 계산 기록 문자열

            // 0으로 나누기 예외 처리
            if (operator.equals("/") && secondNumber == 0) {
                try {
                    throw new ArithmeticException("0으로 나눌 수 없습니다.");
                } catch (ArithmeticException e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
                record = firstNumber + " / " + secondNumber + " = 오류(0으로 나눔)";
            } else {
                // switch expression으로 계산
                double result = switch (operator) {
                    case "+" -> firstNumber + secondNumber;
                    case "-" -> firstNumber - secondNumber;
                    case "*" -> firstNumber * secondNumber;
                    case "/" -> (double) firstNumber / secondNumber;
                    default -> {
                        System.out.println("잘못된 연산자입니다.");
                        yield Double.NaN;
                    }
                };

                // 유효한 연산자일 경우 결과 출력
                if (!Double.isNaN(result)) {
                    System.out.println("결과: " + result);
                    record = firstNumber + " " + operator + " " + secondNumber + " = " + result;
                } else if (record.isEmpty()) {
                    record = firstNumber + " " + operator + " " + secondNumber + " = 오류(잘못된 연산자)";
                }
            }

            // 기록 저장
            if (historyCount < history.length) {
                history[historyCount] = record;
                historyCount++;
            }

            // 계속 여부 입력
            System.out.print("계속하려면 c(continue) / 종료하려면 q(quit) 입력: ");
            String choice = scanner.next().trim().toLowerCase();

            if (choice.equals("q") || choice.equals("quit")) {
                isRun = false;
            }
        }

        // for-each로 기록 출력
        System.out.println("\n=== 계산 기록 ===");
        for (String rec : history) {
            if (rec == null) break; // 저장된 만큼만 출력
            System.out.println(rec);
        }

        scanner.close();
    }
}
