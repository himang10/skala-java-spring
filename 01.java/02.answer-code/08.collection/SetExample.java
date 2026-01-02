import java.util.HashSet;
import java.util.Set;

public class SetExample {
    public static void main(String[] args) {
        // 1. Set 생성 (HashSet 사용)
        Set<String> fruits = new HashSet<>();

        // 2. 데이터 추가
        fruits.add("Apple");
        fruits.add("Banana");
        fruits.add("Orange");
        fruits.add("Banana"); // 중복 → 저장되지 않음

        // 3. 전체 Set 출력
        System.out.println("전체 Set: " + fruits);

        // 4. 포함 여부 확인
        System.out.println("Banana 포함? " + fruits.contains("Banana"));

        // 5. 삭제
        fruits.remove("Orange");
        System.out.println("Orange 삭제 후: " + fruits);

        // 6. 크기 확인
        System.out.println("Set 크기: " + fruits.size());

        // 7. for-each 순회
        System.out.println("for-each 순회:");
        for (String fruit : fruits) {
            System.out.println(" - " + fruit);
        }
    }
}
