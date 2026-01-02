/**
 * Client
 * Factory Method 패턴을 사용하는 클라이언트 코드
 */
public class Main {
    
    public static void main(String[] args) {
        // 뉴욕 피자 스토어 생성
        PizzaStore nyStore = new NYPizzaStore();
        
        // 시카고 피자 스토어 생성
        PizzaStore chicagoStore = new ChicagoPizzaStore();
        
        System.out.println("===== Order 1: NY Cheese Pizza =====");
        Pizza pizza1 = nyStore.orderPizza("cheese");
        System.out.println("Ordered a " + pizza1.getName() + "\n");
        
        System.out.println("===== Order 2: Chicago Pepperoni Pizza =====");
        Pizza pizza2 = chicagoStore.orderPizza("pepperoni");
        System.out.println("Ordered a " + pizza2.getName() + "\n");
        
        System.out.println("===== Order 3: NY Veggie Pizza =====");
        Pizza pizza3 = nyStore.orderPizza("veggie");
        System.out.println("Ordered a " + pizza3.getName() + "\n");
        
        System.out.println("===== Order 4: Chicago Cheese Pizza =====");
        Pizza pizza4 = chicagoStore.orderPizza("cheese");
        System.out.println("Ordered a " + pizza4.getName() + "\n");
    }
}
