class Example {
    public static int INITIAL_COUNT = 10;
    private static int count = 0; // 클래스 변수
    private int instanceId;

    Example() {
        count++;
        instanceId = count + INITIAL_COUNT; // 인스턴스 생성 시 count 증가
    }

    public static int getLastInstanceId() {
        return count + INITIAL_COUNT;
    }

    public static int getCount() {
        return count;
    }

    public int getInstanceId() {
        return instanceId;
    }
}

public class Attribute {
    public static void main(String[] args) {
        System.out.println("current count: {0} last instanceId = {1}", Example.getCount(), Example)

        Example ex1 = new Example();
        Example ex2 = new Example();

        System.out.println("Example 클래스의 인스턴스 생성 후 count: " + Example.getCount()); // 출력: 12
    }
}