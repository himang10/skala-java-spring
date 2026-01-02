/**
 * Concrete Creator
 * 뉴욕 스타일 피자 스토어
 * 팩토리 메서드를 구현하여 뉴욕 스타일의 피자를 생성
 */
public class NYPizzaStore extends PizzaStore {
    
    @Override
    protected Pizza createPizza(String type) {
        Pizza pizza = null;
        
        System.out.println("Creating NY Style " + type + " Pizza");
        
        switch (type.toLowerCase()) {
            case "cheese":
                pizza = new CheesePizza();
                break;
            case "pepperoni":
                pizza = new PepperoniPizza();
                break;
            case "veggie":
                pizza = new VeggiePizza();
                break;
            default:
                System.out.println("Unknown pizza type: " + type);
        }
        
        return pizza;
    }
}
