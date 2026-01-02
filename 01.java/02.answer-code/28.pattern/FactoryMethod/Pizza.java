/**
 * Product Interface
 * 팩토리 메서드로 생성될 객체의 공통 인터페이스
 */
public interface Pizza {
    void prepare();
    void bake();
    void cut();
    void box();
    String getName();
}
