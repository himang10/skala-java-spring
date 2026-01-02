public class MandantoryStock implements Stock {
    private double dividendRate;
    private String name;
    private double price;

    // 자식 생성자에서 super()로 부모 생성자 호출
    public MandantoryStock(String name, double price, double dividendRate) {
        this.name = name;
        this.price = price;
        this.dividendRate = dividendRate;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public void printInfo() {
        System.out.println("[필수종목] 종목: " + name + ", 가격: " + price + "원, 배당률: " + dividendRate + "%");
    }
}
