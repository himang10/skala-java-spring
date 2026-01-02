public class StudentMain {
    public static void main(String[] args) {
        Human human = new Human("Steve");
        human.introduce(); // 출력: 저의 이름은 John입니다.

        Student student = new Student("Alice", "S1234");
        student.introduce(); // 출력: 저의 이름은 Alice이고, 학생 ID는 S1234입니다.

        MiddleSchoolStudent middleStudent = new MiddleSchoolStudent("Bob", "S5678", "ABC 중학교");
        middleStudent.introduce(); // 출력: 저의 이름은 Bob이고, 학생 ID는 S5678입니다. 중학교는 ABC 중학교입니다.

        HighSchoolStudent highStudent = new HighSchoolStudent("John", "S9876", "DEF 고등학교", "수학");
        highStudent.introduce("영어"); // 출력: 저의 이름은 John이고, 학생 ID는 S9876입니다. 고등학교는 DEF 고등학교입니다. 안녕하세요.
    }
}
