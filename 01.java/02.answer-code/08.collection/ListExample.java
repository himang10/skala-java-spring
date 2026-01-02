import java.util.ArrayList;
import java.util.List;

public class ListExample {
    public static void main(String[] args) {
        // 1. List 생성 (ArrayList 사용)
        List<String> fruits = new ArrayList<>();

        // 2. 데이터 추가
        fruits.add("Apple");
        fruits.add("Banana");
        fruits.add("Orange");
        fruits.add("Banana"); // 중복 허용

        // 3. 데이터 출력
        System.out.println("전체 리스트: " + fruits);

        // 4. 인덱스로 접근
        System.out.println("첫 번째 요소: " + fruits.get(0));

        // 5. 포함 여부 확인
        System.out.println("Banana 포함? " + fruits.contains("Banana"));

        // 6. 삭제
        fruits.remove("Orange");
        System.out.println("Orange 삭제 후: " + fruits);

        // 7. 크기 확인
        System.out.println("리스트 크기: " + fruits.size());

        // 8. for-each 순회
        System.out.println("for-each 순회:");
        for (String fruit : fruits) {
            System.out.println(" - " + fruit);
        }

        // 9. 인덱스 기반 for문 순회
        System.out.println("인덱스 기반 for문:");
        for (int i = 0; i < fruits.size(); i++) {
            System.out.println(i + "번째: " + fruits.get(i));
        }
    }
}
