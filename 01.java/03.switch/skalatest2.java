package skalajava;

public class skalatest2 {
    public static void main(String[] args) {
        executeLogin(LoginChannel.FACEBOOK);
    }
    public static void executeLogin(LoginChannel channel) {
        switch(channel) {
            case FACEBOOK:
                facebookLogin();
                break;
            case KAKAO:
                kakaoLogin();
                break;
            case APPLE:
                appleLogin();
                break;
            case NAVER:
                naverLogin();
                break;
            case DEFAULT:
                defaultLogin();
                break;    
        }
    }
    private static void facebookLogin() {
        System.out.println("FACEBOOK Login");
    }
    private static void kakaoLogin() {
        System.out.println("FACEBOOK Login");
    }
    private static void appleLogin() {
        System.out.println("FACEBOOK Login");
    }
    private static void naverLogin() {
        System.out.println("FACEBOOK Login");
    }
    private static void defaultLogin() {
        System.out.println("FACEBOOK Login");
    }
}
