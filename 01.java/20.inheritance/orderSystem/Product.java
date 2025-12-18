public class Product {
    private String productName;
    private double price;    // 개당 가격
    private int quantity;    // 수량

    public Product(String productName, double price, int quantity) {
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }

    public double calculateTotalPrice() {
        return price * quantity;
    }

    // Getter/Setter
    public String getProductName() { return productName; }

    public double getPrice() { return price; }
    public void setPrice(double price) {
        if (price < 0) throw new IllegalArgumentException("가격은 0 이상이어야 합니다.");
        this.price = price;
    }

    public int getQuantity() { return quantity; }

    @Override
    public String toString() {
        return String.format("%s (개당 %.2f원, 수량 %d개, 합계 %.2f원)",
                productName, price, quantity, calculateTotalPrice());
    }
}
