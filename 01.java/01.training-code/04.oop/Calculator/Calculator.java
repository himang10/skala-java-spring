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
                // 여기에 While, calculate() 호출, 계산 기록 등록, 종료 조건 처리 코드 작성
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