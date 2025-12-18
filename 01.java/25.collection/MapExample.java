import java.util.HashMap;
import java.util.Map;

public class MapExample {
    public static void main(String[] args) {
        // 1. Map 생성 (HashMap 사용)
        Map<Integer, String> fruits = new HashMap<>();

        // 2. 데이터 추가 (key, value)
        fruits.put(1, "Apple");
        fruits.put(2, "Banana");
        fruits.put(3, "Orange");
        fruits.put(4, "Banana"); // 값은 중복 허용, key는 중복 불가

        // 3. 전체 Map 출력
        System.out.println("전체 Map: " + fruits);

        // 4. key로 value 조회
        System.out.println("Key=1의 값: " + fruits.get(1));

        // 5. 포함 여부 확인
        System.out.println("Banana 포함? " + fruits.containsValue("Banana"));

        // 6. Orange 삭제
        fruits.remove(3);
        System.out.println("Orange 삭제 후: " + fruits);

        // 7. 크기 확인
        System.out.println("Map 크기: " + fruits.size());

        // 8. keySet 순회
        System.out.println("keySet 순회:");
        for (Integer key : fruits.keySet()) {
            System.out.println(key + " => " + fruits.get(key));
        }

        // 9. entrySet 순회
        System.out.println("entrySet 순회:");
        for (Map.Entry<Integer, String> entry : fruits.entrySet()) {
            System.out.println(entry.getKey() + " => " + entry.getValue());
        }
    }
}
