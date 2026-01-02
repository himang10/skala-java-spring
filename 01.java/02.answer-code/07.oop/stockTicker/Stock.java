class Stock extends Asset {
    protected boolean hasVotingRights = true;
    protected boolean hasDividendPriority = false;

    public Stock(String name, double price) {
        super(name, price);
    }

    public void updatePrice(double newPrice) {
        this.price = newPrice;
        System.out.println(name + " 가격이 " + price + "원으로 변경되었습니다.");
    }

    @Override
    public void printInfo() {
        System.out.println("[일반주] 종목: " + name +
            " / 현재가: " + price + "원" +
            " / 의결권: " + (hasVotingRights ? "있음" : "없음") +
            " / 배당우선권: " + (hasDividendPriority ? "있음" : "없음"));
    }
}

