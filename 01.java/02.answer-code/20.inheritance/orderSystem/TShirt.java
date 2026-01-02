public class TShirt extends Clothing {
    public TShirt(String productName, double price, int quantity, String size, String material) {
        super(productName, price, quantity, size, material);
    }

    /** 사이즈 변경 */
    public void changeSize(String newSize) {
        setSize(newSize);
    }

    @Override
    public String toString() {
        return super.toString() + " [품목: 티셔츠]";
    }
}
