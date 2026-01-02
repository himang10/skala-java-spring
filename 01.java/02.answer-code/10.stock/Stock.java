
public class Stock {
    private String stockName;
    private double stockPrice;

    // 생성자
    public Stock(String stockName, double stockPrice) {
        this.stockName = stockName;
        this.stockPrice = stockPrice;
    }

    // Getter 메서드
    public String getStockName() {
        return stockName;
    }

    public double getStockPrice() {
        return stockPrice;
    }

    // Setter 메서드
    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public void setStockPrice(double stockPrice) {
        this.stockPrice = stockPrice;
    }

    // toString() 메서드 오버라이딩
    @Override
    public String toString() {
        return stockName + ":" + stockPrice;
    }
}
