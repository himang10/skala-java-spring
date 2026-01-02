public class Laptop extends Electronics {
    private int ramGB; // 현재 RAM 용량(GB)

    public Laptop(String productName, double price, int quantity, int warrantyMonths, int ramGB) {
        super(productName, price, quantity, warrantyMonths);
        this.ramGB = ramGB;
    }

    /** RAM 업그레이드 (예: 8GB -> 16GB로 +8GB) */
    public void upgradeRAM(int addGB) {
        if (addGB <= 0) throw new IllegalArgumentException("추가 RAM은 1GB 이상이어야 합니다.");
        this.ramGB += addGB;
    }

    public int getRamGB() { return ramGB; }
    public void setRamGB(int ramGB) {
        if (ramGB <= 0) throw new IllegalArgumentException("RAM은 1GB 이상이어야 합니다.");
        this.ramGB = ramGB;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" [RAM %dGB]", ramGB);
    }
}
