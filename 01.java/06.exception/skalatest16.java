package skalajava;

public class skalatest16 {
    public static void main(String[] args) {
        int size = 5;
        int floor = 1;
        for (int i = floor; i <= size; i++) {
            for (int j=0; j < size - i; j++) {
                System.out.print(" ");
            }
            for(int j=0; j < 2 * i - 1; j++) {
                System.out.print("*");
            }
            System.out.println();
        }
    }
}
