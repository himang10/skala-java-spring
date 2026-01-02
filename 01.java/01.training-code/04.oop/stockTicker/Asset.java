// 공통 추상 클래스: 자산
abstract class Asset {
    protected String name;
    protected double price;

    public Asset(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public abstract void printInfo();
}

