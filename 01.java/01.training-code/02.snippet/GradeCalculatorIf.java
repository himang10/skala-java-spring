import java.util.Scanner;

public class GradeCalculatorIf {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("점수를 입력하세요 (0~100): ");
        Integer score = scanner.nextInt();

        if (score < 0 || score > 100) {
            System.out.println("점수는 0에서 100 사이여야 합니다.");
        } else {
            char grade;

            if (score >= 90) {
                grade = 'A';
            } else if (score >= 80) {
                grade = 'B';
            } else if (score >= 70) {
                grade = 'C';
            } else if (score >= 60) {
                grade = 'D';
            } else {
                grade = 'F';
            }

            System.out.println("학점: " + grade);
        }

        scanner.close();
    }
}
