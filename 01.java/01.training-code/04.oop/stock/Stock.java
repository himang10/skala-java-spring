public class Stock {
    protected String name;
    protected double price;

    public Stock(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public void printInfo() {
        System.out.println("[일반주] 종목: " + name + ", 가격: " + price + "원");
    }
}
