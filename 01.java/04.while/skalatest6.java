package skalajava;

public class skalatest6 {
    public static void main(String[] args) {
        printOddNumbers();
    }
    public static void printOddNumbers() {
        int number = 0;
        while (number < 10) {
            number++;
            if(number % 2 == 0) {
                continue;
            }
            System.out.println("홀수="+number);
        }
    }
}
