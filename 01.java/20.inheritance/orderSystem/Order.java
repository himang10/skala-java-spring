import java.util.ArrayList;
import java.util.List;

public class Order {
    private final List<Product> productList = new ArrayList<>();

    public void addProduct(Product product) {
        if (product == null) throw new IllegalArgumentException("product는 null일 수 없습니다.");
        productList.add(product);
    }

    public double getTotalOrderPrice() {
        return productList.stream()
                .mapToDouble(Product::calculateTotalPrice)
                .sum();
    }

    public List<Product> getProductList() {
        return new ArrayList<>(productList); // 방어적 복사
    }

    public void printSummary() {
        System.out.println("=== 주문 내역 ===");
        productList.forEach(p -> System.out.println("- " + p));
        System.out.printf("총 주문 금액: %.2f원%n", getTotalOrderPrice());
    }
}
