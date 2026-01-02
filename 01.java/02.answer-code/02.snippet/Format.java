import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 문자열 포맷팅 예제
 * @author skala
 * @version 1.0
 * @since 2025-08-13
 */
public class Format {
    public static void main(String[] args) {
        String name = "스칼라";
        int age = 30;
        String formatted = String.format("이름: %s, 나이: %d", name, age);
        System.out.println(formatted);  // 이름: 스칼라, 나이: 30

    Date today = new Date();
    String msg = MessageFormat.format("오늘은 {0,date,yyyy-MM-dd}입니다.", today);
    System.out.println(msg);    // 출력 예: 오늘은 2025-07-23입니다.



    }
}
