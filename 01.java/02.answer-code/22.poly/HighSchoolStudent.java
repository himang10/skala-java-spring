// MiddleStudent 클래스 (Student 클래스를 상속)
class HighSchoolStudent extends Student {
    String highSchool;
    String subject;

    // 생성자
    public HighSchoolStudent(String name, String studentId, String highSchool, String subject) {
        super(name, studentId); // 부모 클래스의 생성자 호출
        this.highSchool = highSchool;
        this.subject = subject;
    }

    // introduce 메서드 오버라이딩
    @Override
    void introduce() {
        System.out.println("저의 이름은 " + name + "이고, 학생 ID는 " + studentId + "입니다. 중학교는 " + highSchool + "입니다.");
    }

    // introduce 메서드 오버로딩
    void introduce(String message) {
        introduce();
        System.out.println("제 전공은 " + subject + "입니다." + "나는 " + message + "을 추가하고 싶습니다");
    }
}
