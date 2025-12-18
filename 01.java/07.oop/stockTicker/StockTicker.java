public class StockTicker {

    private Stock scalaEdu;
    private PreferredStock scalaAI;

    public StockTicker() {
        // 종목 초기화
        this.scalaEdu = new Stock("스칼라 에듀", 15000);
        this.scalaAI = new PreferredStock("스칼라 AI", 17500, 5.0);
    }


    // 주식 정보 출력 및 가격 변경
    public void printTicker() {
        // 초기 정보 출력
        scalaEdu.printInfo();
        scalaAI.printInfo();
    }

    public void updateStock(int price) {
        scalaEdu.updatePrice(15800);
    }


    public void updatePreferredStock(int price) {
        scalaAI.updatePrice(18000);
    }

}

