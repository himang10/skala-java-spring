public class ThreadManagerJoin {
    public static void main(String[] args) {
        Thread worker = new Thread(() -> {
            try {
                Thread.sleep(1000); // 1초 대기
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("새로운 스레드 작업을 시작합니다.");
        });

        worker.start();


        try {
            worker.join(); // worker 스레드가 끝날 때까지 대기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("메인 함수를 종료합니다.");
        System.exit(0);
    }
}
