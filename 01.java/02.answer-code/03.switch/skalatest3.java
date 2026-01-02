package skalajava;

public class skalatest3 {
    public static void main(String[] args) {
        executeLogin(LoginChannel.FACEBOOK);
    }
    public static void executeLogin(LoginChannel channel) {
        switch (channel) {
            case FACEBOOK -> facebookLogin();
            case KAKAO -> kakaoLogin();
            case APPLE -> appleLogin();
            case NAVER -> naverLogin();
            case DEFAULT -> defaultLogin();
        }
    }
    private static void facebookLogin() {
        System.out.println("FACEBOOK Login");
    }
    private static void kakaoLogin() {
        System.out.println("KAKAO Login");
    }
    private static void appleLogin() {
        System.out.println("Apple Login");
    }
    private static void naverLogin() {
        System.out.println("Naver Login");
    }
    private static void defaultLogin() {
        System.out.println("Default Login");
    }
}
