/**
 * OrderController 클래스
 * @Inject로 OrderService 주입받음
 */
public class OrderController {
    @Inject
    private OrderService orderService;  // 의존성 주입 대상
    
    public void handleOrder(String orderName) {
        if (orderService != null) {
            orderService.processOrder(orderName);
        } else {
            System.out.println(" OrderService가 주입되지 않음");
        }
    }
    
    public OrderService getOrderService() {
        return orderService;
    }
}
