public class Clothing extends Product {
    private String size;     // 예: S, M, L
    private String material; // 예: Cotton, Wool

    public Clothing(String productName, double price, int quantity, String size, String material) {
        super(productName, price, quantity);
        this.size = size;
        this.material = material;
    }

    /** 할인 적용: 0~100 사이 퍼센트 */
    public void applyDiscount(double percent) {
        if (percent < 0 || percent > 100) throw new IllegalArgumentException("할인율은 0~100이어야 합니다.");
        double discounted = getPrice() * (1 - percent / 100.0);
        setPrice(discounted);
    }

    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }

    public String getMaterial() { return material; }
    public void setMaterial(String material) { this.material = material; }

    @Override
    public String toString() {
        return super.toString() + String.format(" [사이즈 %s, 소재 %s]", size, material);
    }
}
