// MiddleStudent 클래스 (Student 클래스를 상속)
class MiddleSchoolStudent extends Student {
    String middleSchool;

    // 생성자
    public MiddleSchoolStudent(String name, String studentId, String middleSchool) {
        super(name, studentId); // 부모 클래스의 생성자 호출
        this.middleSchool = middleSchool;
    }

    // introduce 메서드 오버라이딩
    @Override
    void introduce() {
        System.out.println("저의 이름은 " + name + "이고, 학생 ID는 " + studentId + "입니다. 중학교는 " + middleSchool + "입니다.");
    }
}
