public class PreferredStock extends Stock {
    private double dividendRate;

    // 자식 생성자에서 super()로 부모 생성자 호출
    public PreferredStock(String name, double price, double dividendRate) {
        super(name, price); // 부모 생성자 호출
        this.dividendRate = dividendRate;
    }

    // 메서드 오버라이딩
    @Override
    public void printInfo() {
        System.out.println("[우선주] 종목: " + name + ", 가격: " + price + "원, 배당률: " + dividendRate + "%");
    }
}
