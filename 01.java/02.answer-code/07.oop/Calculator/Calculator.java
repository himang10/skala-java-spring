import java.util.Scanner;

// 추상 클래스: 반복 실행과 기록 출력 담당
abstract class Calculator {
    protected String[] history = new String[100]; // 기록 저장
    protected int historyCount = 0;

    // 반복 실행
    public void run() {
        try (Scanner scanner = new Scanner(System.in)) {
            boolean isRun = true;
            while (isRun) {
                System.out.print("첫 번째 숫자: ");
                int firstNumber = scanner.nextInt();

                System.out.print("연산자(+ - * /): ");
                String operator = scanner.next().trim();

                System.out.print("두 번째 숫자: ");
                int secondNumber = scanner.nextInt();

                // 계산 실행
                String record = calculate(firstNumber, operator, secondNumber);

                // 기록 저장
                if (historyCount < history.length) {
                    history[historyCount] = record;
                    historyCount++;
                }

                // 계속 여부
                System.out.print("계속하려면 c(continue) / 종료하려면 q(quit) 입력: ");
                String choice = scanner.next().trim().toLowerCase();
                if (choice.equals("q") || choice.equals("quit")) {
                    isRun = false;
                }
            }
        }

        // 기록 출력
        printHistory();
    }

    // 기록 출력
    protected abstract void printHistory();

    // 개별 계산 로직 (구현 클래스에서 작성)
    protected abstract String calculate(int firstNumber, String operator, int secondNumber);
}