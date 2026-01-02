interface AInterface {
    default String info() {
        return "A 입니다.";
    }
}

interface BInterface {
    default String info() {
        return "B 입니다.";
    }
}

interface CInterface {
    default String myInfo() {
        return "C 입니다.";
    }
}

// AInterface와 BInterface를 모두 구현한 클래스
class MultiInherit implements AInterface, BInterface {

    // AInterface와 BInterface에 동일한 메서드가 있을 경우
    // 어떤 메서드를 사용할지 명시적으로 지정해야 함
    @Override
    public String info() {
        return AInterface.super.info();
    }
}

class MultiInherit2 implements AInterface, CInterface {
}

public class MultiInheritMain {
    public static void main(String[] args) {
        // AInterface와 BInterface를 모두 구현한 클래스의 인스턴스 생성
        MultiInherit multiInherit = new MultiInherit();
        System.out.println("multiInherit.toString(): " + multiInherit.toString()); // 출력: A 입니다.
        System.out.println(multiInherit.info()); // 출력: A 입니다.


        MultiInherit2 multiInherit2 = new MultiInherit2();
        System.out.println("multiInherit2.toString(): " + multiInherit2.toString()); // 출력: A 입니다.
        System.out.println(multiInherit2.info()); // 출력: A 입니다.
        System.out.println(multiInherit2.myInfo()); // 출력: C 입니다.
    }
    
}
