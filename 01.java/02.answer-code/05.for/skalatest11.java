package skalajava;
public class skalatest11 {
    public static void main(String[] args) {
        printGuGuDan();
    }
    public static void printGuGuDan() {
        for(int i = 1; i <= 9; i++) {
            for (int j = 1; j <= 9; j++) {
                System.out.print(i * j + " " );
            }
            System.out.println();
        }
    }
}
