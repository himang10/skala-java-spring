package skalajava;
import java.util.Random;
public class skalatest5 {
    public static void main(String[] args) {
        Random random = new Random();
        int number = 100;
        while(true) {
            if(number <= 5){
                System.out.println("무한루프탈출" + number);
                break;
            }
            number = random.nextInt(100) + 1;
            System.out.println("현재 숫자 = " + number);
        }
    }
}
