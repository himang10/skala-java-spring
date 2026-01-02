import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.Queue;

public class QueuePollExample {
    public static void main(String[] args) {
        Queue<String> queue = new ArrayDeque<>();

        queue.offer("Apple");
        queue.offer("Banana");
        queue.offer("Orange");
        queue.offer("Grape");

        System.out.println("초기 Queue: " + queue);

        // 4. FIFO 확인: 맨 앞 요소 확인 (peek)
        System.out.println("peek(): " + queue.peek());

        // for-each는 요소 삭제와 동시 사용 불가
        // while(!isEmpty()) + poll() 로 안전하게 FIFO 꺼내기
        while (!queue.isEmpty()) {
            String item = queue.poll();  // FIFO로 하나씩 꺼냄
            System.out.println("꺼낸 요소: " + item + ", 남은 Queue: " + queue);
        }
    }
}


