public class MyCalculator extends Calculator {

    // 기록 출력
    protected void printHistory() {
        System.out.println("\n=== 계산 기록 ===");
        for (String rec : history) {
            if (rec == null) break;
            System.out.println(rec);
        }
    }

    @Override
    protected String calculate(int firstNumber, String operator, int secondNumber) {
        String record = "";

        if (operator.equals("/") && secondNumber == 0) {
            try {
                throw new ArithmeticException("0으로 나눌 수 없습니다.");
            } catch (ArithmeticException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
            record = firstNumber + " / " + secondNumber + " = 오류(0으로 나눔)";
        } else {
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

            if (!Double.isNaN(result)) {
                System.out.println("결과: " + result);
                record = firstNumber + " " + operator + " " + secondNumber + " = " + result;
            } else if (record.isEmpty()) {
                record = firstNumber + " " + operator + " " + secondNumber + " = 오류(잘못된 연산자)";
            }
        }

        return record;
    }
}