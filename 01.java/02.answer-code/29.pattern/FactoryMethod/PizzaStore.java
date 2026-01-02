/**
 * Creator (Abstract Class)
 * 팩토리 메서드를 선언하는 추상 클래스
 */
public abstract class PizzaStore {
    
    /**
     * 피자 주문 메서드 (템플릿 메서드)
     * 객체 생성 과정은 정의하되, 실제 객체 생성은 서브클래스에 위임
     */
    public Pizza orderPizza(String type) {
        Pizza pizza;
        
        // Factory Method 호출 - 서브클래스가 구현
        pizza = createPizza(type);
        
        if (pizza != null) {
            pizza.prepare();
            pizza.bake();
            pizza.cut();
            pizza.box();
        }
        
        return pizza;
    }
    
    /**
     * Factory Method
     * 서브클래스에서 구현해야 하는 메서드
     * 객체 생성의 책임을 서브클래스에 위임
     */
    protected abstract Pizza createPizza(String type);
}
