public class Electronics extends Product {
    private int warrantyPeriodMonths; // 보증기간(개월)

    public Electronics(String productName, double price, int quantity, int warrantyPeriodMonths) {
        super(productName, price, quantity);
        this.warrantyPeriodMonths = warrantyPeriodMonths;
    }

    /** 보증기간 연장 */
    public void extendWarranty(int additionalMonths) {
        if (additionalMonths <= 0) throw new IllegalArgumentException("연장 개월은 1개월 이상이어야 합니다.");
        this.warrantyPeriodMonths += additionalMonths;
    }

    public int getWarrantyPeriodMonths() { return warrantyPeriodMonths; }
    public void setWarrantyPeriodMonths(int warrantyPeriodMonths) {
        if (warrantyPeriodMonths < 0) throw new IllegalArgumentException("보증기간은 음수일 수 없습니다.");
        this.warrantyPeriodMonths = warrantyPeriodMonths;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" [보증 %d개월]", warrantyPeriodMonths);
    }
}
