package skalajava;

public class skalatest1 {
    public static void main(String[] args) {
        String season = "summer";
        switch (season) {
            case "spring":
                System.out.println("봄");
                break;
            case "summer":
                System.out.println("여름");
                break;
            case "fall":
                System.out.println("가을");
                break;
            case "winter":
                System.out.println("겨울");
                break;
            default:
                System.out.println("No Season");
                break;
        }
    }
}
