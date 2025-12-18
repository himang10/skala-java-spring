public class StudentMain {
    public static void main(String[] args) {
        Human human = new Human("Steve");
        human.introduce(); // 출력: 저의 이름은 John입니다.

        Student student = new Student("Alice", "S1234");
        student.introduce(); // 출력: 저의 이름은 Alice이고, 학생 ID는 S1234입니다.
    }
}
