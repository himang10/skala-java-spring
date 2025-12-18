import java.util.Optional;

public class OptionalExample {
    public static void main(String[] args) {
        // 1. 값이 있는 경우
        Optional<String> optName = Optional.of("홍길동");

        // 값이 있으면 출력
        optName.ifPresent(name -> System.out.println("이름: " + name));

        // 값이 없으면 기본값 사용
        String result1 = optName.orElse("기본 이름");
        System.out.println("orElse 결과: " + result1);

        // 2. 값이 null일 수도 있는 경우
        String nullableName = null;
        Optional<String> optNullable = Optional.ofNullable(nullableName);

        // 값이 없으므로 기본값 반환
        String result2 = optNullable.orElse("이름 없음");
        System.out.println("orElse 결과 (null 처리): " + result2);

        // 3. orElseGet → 필요할 때만 실행
        String result3 = optNullable.orElseGet(() -> "동적으로 기본값 생성");
        System.out.println("orElseGet 결과: " + result3);

        // 4. 값이 없으면 예외 발생시키기
        try {
            String result4 = optNullable.orElseThrow(() -> new IllegalArgumentException("값이 없습니다!"));
            System.out.println("orElseThrow 결과: " + result4);
        } catch (Exception e) {
            System.out.println("예외 발생: " + e.getMessage());
        }

        // 5. map() 으로 값 변환
        Optional<String> upper = optName.map(String::toUpperCase);
        System.out.println("map 결과: " + upper.orElse("없음"));
    }
}

