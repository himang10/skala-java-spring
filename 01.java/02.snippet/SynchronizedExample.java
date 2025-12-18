class BankAccount {
    private int balance = 1000; // 잔액

    // synchronized 메서드
    public synchronized void withdraw(int amount) {
        if (balance >= amount) {
            System.out.println(Thread.currentThread().getName() + " 출금 시도: " + amount);
            try {
                Thread.sleep(100); // 출금 처리 지연(동시성 문제 시뮬레이션)
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            balance -= amount;
            System.out.println(Thread.currentThread().getName() + " 출금 완료. 남은 잔액: " + balance);
        } else {
            System.out.println(Thread.currentThread().getName() + " 출금 실패. 잔액 부족");
        }
    }

    public int getBalance() {
        return balance;
    }
}

public class SynchronizedExample {
    public static void main(String[] args) {
        BankAccount account = new BankAccount();

        // 두 개의 스레드가 동시에 출금
        Thread t1 = new Thread(() -> account.withdraw(800), "스레드-1");
        Thread t2 = new Thread(() -> account.withdraw(800), "스레드-2");

        t1.start();
        t2.start();
    }
}
