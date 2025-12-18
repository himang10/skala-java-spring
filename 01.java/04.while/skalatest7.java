package skalajava;

public class skalatest7 {
    public static void main(String[] args) {
        printOddNumbers();
    }
    public static void printOddNumbers() {
        int number = 0;
        do {
            number++;
            if(number % 2 == 0) {
                continue;
            }
            System.out.println("홀수="+number);
        } while (number < 10);
    }
}
