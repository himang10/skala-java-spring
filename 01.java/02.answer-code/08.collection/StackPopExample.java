import java.util.ArrayDeque;
import java.util.Stack;

public class StackPopExample {
    public static void main(String[] args) {
        Stack<String> stack = new ArrayDeque<>();

        // 데이터 추가 (push → 맨 위에 쌓임)
        stack.push("Apple");
        stack.push("Banana");
        stack.push("Orange");
        stack.push("Grape");

        System.out.println("초기 Stack: " + stack);

        // 맨 위 요소 확인 (peek)
        System.out.println("peek(): " + stack.peek());

        // FILO 동작 확인: pop()으로 맨 위부터 꺼내기
        while (!stack.isEmpty()) {
            String item = stack.pop();  // FILO로 하나씩 꺼냄
            System.out.println("꺼낸 요소: " + item + ", 남은 Stack: " + stack);
        }
    }
}
