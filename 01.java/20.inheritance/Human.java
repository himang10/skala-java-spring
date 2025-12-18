public class Human {
    String name;

    // 생성자
    public Human(String name) {
        this.name = name;
    }

    // introduce 메서드
    void introduce() {
        System.out.println("저의 이름은 " + name + "입니다.");
    }
}
