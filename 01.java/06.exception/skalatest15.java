package skalajava;
public class skalatest15 {
    public static void main(String[] args) {
        int size = 5;
        int floor = 1;
        while (floor <= size) {
            for (int i=0; i < floor; i++) {
                System.out.print("*");
            }
            System.out.println();
            floor++;
        }
    }
}
