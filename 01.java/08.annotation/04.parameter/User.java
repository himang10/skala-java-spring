/**
 * 샘플 JSON 데이터를 가진 User 클래스
 */
public class User {
    public String userName;
    public int userAge;
    
    public User(String userName, int userAge) {
        this.userName = userName;
        this.userAge = userAge;
    }
    
    public String toString() {
        return "User{" + "userName='" + userName + "', userAge=" + userAge + "}";
    }
}
