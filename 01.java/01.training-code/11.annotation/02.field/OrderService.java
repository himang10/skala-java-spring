/**
 * OrderService 클래스
 * 의존성 주입 대상
 */
public class OrderService {
    public void processOrder(String orderName) {
        System.out.println(" 주문 처리 중: " + orderName);
    }
    
    public String getServiceName() {
        return "OrderService";
    }
}
