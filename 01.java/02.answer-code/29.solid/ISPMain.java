// 작업 관련 인터페이스
interface Workable {
    void work();
}

// 식사 관련 인터페이스
interface Eatable {
    void eat();
}

// 사람은 일을 하고 밥도 먹을 수 있음
class HumanWorker implements Workable, Eatable {
    public void work() { System.out.println("사람이 일을 합니다."); }
    public void eat() { System.out.println("사람이 밥을 먹습니다."); }
}

// 로봇은 일을 하지만, 밥을 먹지 않음 (Eatable을 구현하지 않음)
class RobotWorker implements Workable {
    public void work() { System.out.println("로봇이 작업을 합니다."); }
}

// 사용 예시
public class ISPMain {
    public static void main(String[] args) {
        Workable human = new HumanWorker();
        Workable robot = new RobotWorker();

        human.work(); // 사람: 일 가능
        ((Eatable) human).eat(); // 사람: 식사 가능

        robot.work(); // 로봇: 일 가능
    }
}
