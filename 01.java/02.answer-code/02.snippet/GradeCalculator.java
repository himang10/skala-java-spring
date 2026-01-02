import java.util.Scanner;

public class GradeCalculator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // 점수 입력 받기
        System.out.print("점수를 입력하세요 (0~100): ");
        int score = scanner.nextInt();

        String grade;

        // 학점 계산
        if (score >= 90) {
            grade = "A";
        } else if (score >= 80) {
            grade = "B";
        } else if (score >= 70) {
            grade = "C";
        } else if (score >= 60) {
            grade = "D";
        } else {
            grade = "F";
        }

        // 결과 출력
        System.out.println("당신의 학점은: " + grade);

        scanner.close();
    }
}

