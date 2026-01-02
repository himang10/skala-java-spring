// 우선주 클래스 (Preferred Stock)
class PreferredStock extends Stock {
    private double dividendRate;

    public PreferredStock(String name, double price, double dividendRate) {
        super(name, price);
        this.dividendRate = dividendRate;
        this.hasVotingRights = false;
        this.hasDividendPriority = true;
    }

    @Override
    public void printInfo() {
        System.out.println("[우선주] 종목: " + name +
            " / 현재가: " + price + "원" +
            " / 배당률: " + dividendRate + "%" +
            " / 의결권: " + (hasVotingRights ? "있음" : "없음") +
            " / 배당우선권: " + (hasDividendPriority ? "있음" : "없음"));
    }
}

