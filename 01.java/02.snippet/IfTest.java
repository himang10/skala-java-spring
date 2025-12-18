import java.util.ArrayList;
import java.util.List;

/**
 * if 문 테스트 클래스
 * @version 1.0
 * @since 2025-08-13
 * @author skala
 */
public class IfTest {
    private static List<String> listOfStrings = new ArrayList<>();

    /**
     * 주어진 문자열이 리스트에 존재하는지 확인합니다.
     * @param value 확인할 문자열
     * @return 존재하면 true, 그렇지 않으면 false
     */

    public static boolean hasValue(String value) {
        return listOfStrings.contains(value);
    }

    public static void main(String[] args) {

        // TODO: 초기값 설정
        listOfStrings.add("A");
        listOfStrings.add("B");
        listOfStrings.add("C");
        
        if (listOfStrings.contains("D")) {
            System.out.println("D is in the list");
        } else {
            System.out.println("D is not in the list");
        }

        if (hasValue("C")) {
            System.out.println("C is in the list");
        } else {
            System.out.println("C is not in the list");
        }

    }
    
}
