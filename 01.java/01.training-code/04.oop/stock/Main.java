public class Main {
    static void printStockInfo(Stock stock) {
        stock.printInfo();
    }

    public static void main(String[] args) {
        Stock stock1 = new Stock("삼성전자", 80000);
        Stock stock2 = new PreferredStock("LG전자", 60000, 5.0);

        stock1.printInfo();
        stock2.printInfo();

        System.out.println("-----");

        printStockInfo(new Stock("SAKALA", 80000))  ;
        printStockInfo(new PreferredStock("Netflix", 60000, 10.0));
    }
}
