// 실행 예제
public class MStockInfo {
    public static void main(String[] args) {
        StockTicker ticker = new StockTicker();

        ticker.printTicker();

        ticker.updateStock(15800);
        ticker.updatePreferredStock(18000);

        System.out.println("\n[가격 변경 후 정보]");
        ticker.printTicker();
    }
}

