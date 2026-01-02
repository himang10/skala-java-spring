import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FormatPrintExample {
    public static void main(String[] args) {
        // 숫자 포맷터 (통화)
        DecimalFormat decimalFormat = new DecimalFormat("₩#,##0.00");
        
        // 날짜 포맷터
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 예시 데이터
        double amount = 12345.678;
        LocalDateTime now = LocalDateTime.now();

        // 각각 포맷 적용
        String formattedAmount = decimalFormat.format(amount);
        String formattedDate = dateTimeFormatter.format(now);

        // println에서 함께 출력
        System.out.println("날짜: " + formattedDate + ", 금액: " + formattedAmount);
    }
}