import java.util.Optional;

public class OptionalParamExample {

    // Optional<String> 을 입력받아 처리하는 메서드
    public static void printName(Optional<String> optName) {
        // 값이 있으면 대문자로 출력, 없으면 "이름 없음"
        String name = optName
                .map(String::toUpperCase)  // 값이 있으면 대문자로 변환
                .orElse("이름 없음");        // 값이 없으면 기본값
        System.out.println("결과: " + name);
    }

        // Optional<String> 을 입력받아 처리하는 메서드
        public static void emailCheck(Optional<String> email) {
            // 값이 있으면 대문자로 출력, 없으면 "이름 없음"
            email.filter(s -> s.contains("@"))       // 유효한 이메일만
                .map(String::toLowerCase)           // 소문자로 변환
                .ifPresent(s -> System.out.println("메일 발송: " + s));
        }

    public static void main(String[] args) {
        // 값이 있는 경우
        Optional<String> email1 = Optional.of("hong@gmail.com");
        emailCheck(email1);

        // 값이 없는 경우
        Optional<String> email2 = Optional.empty();
        printName(email2);   // 결과: 이름 없음

        // null 가능성을 가진 변수
        String nullableName = "honggildong";
        Optional<String> name3 = Optional.ofNullable(nullableName);
        emailCheck(name3);
        printName(name3);   // 결과: 이름 없음
    }
}

