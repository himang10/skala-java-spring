package skalajava;

public class skalatest4 {
    
    public static void main(String[] args) {
        int age = 0;
        while (age < 19) {
            age++;
            System.out.println("현재 나이는 "+ age + "살 입니다");
            if(age == 19) {
                System.out.println("이제 성인입니다.");
            }
        }
    }
}
