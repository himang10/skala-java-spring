public class Main {
    static void printStockInfo(Stock stock) {
        stock.printInfo();
    }

    public static void main(String[] args) {
        printStockInfo(new MandantoryStock("삼성전자", 80000, 10.0))  ;
        printStockInfo(new PreferredStock("Netflix", 60000, 10.0));
    }
}
