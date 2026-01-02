/**
 * Field Injection 예제
 * 
 * 1. OrderController에 @Inject private OrderService 필드 선언
 * 2. SimpleContainer.inject()로 자동 주입
 * 3. 주입된 orderService 사용
 */
public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("\n=== Field Injection 예제 ===\n");
        
        // 1. OrderController 객체 생성
        OrderController controller = new OrderController();
        System.out.println("OrderController 생성");
        System.out.println("    주입 전: orderService = " + controller.getOrderService());
        
        // 2. 의존성 자동 주입
        System.out.println("\n    SimpleContainer.inject() 호출");
        SimpleContainer.inject(controller);
        
        // 3. 주입 확인 및 사용
        System.out.println("\n    주입 후: orderService = " + controller.getOrderService());
        
        // 4. 주입된 서비스 사용
        System.out.println("\n    주입된 서비스 사용");
        controller.handleOrder("노트북 주문");
        controller.handleOrder("마우스 주문");
        
        System.out.println("\n    완료!\n");
    }
}
