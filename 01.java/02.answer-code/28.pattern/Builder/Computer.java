/**
 * Product Class
 * Builder 패턴으로 생성될 복잡한 객체
 */
public class Computer {
    // 필수 파라미터
    private final String cpu;
    private final String ram;
    
    // 선택적 파라미터
    private final String storage;
    private final String graphicsCard;
    private final String powerSupply;
    private final String motherboard;
    private final String coolingSystem;
    private final boolean hasWifi;
    private final boolean hasBluetooth;
    
    // Private constructor - Builder를 통해서만 생성 가능
    private Computer(Builder builder) {
        this.cpu = builder.cpu;
        this.ram = builder.ram;
        this.storage = builder.storage;
        this.graphicsCard = builder.graphicsCard;
        this.powerSupply = builder.powerSupply;
        this.motherboard = builder.motherboard;
        this.coolingSystem = builder.coolingSystem;
        this.hasWifi = builder.hasWifi;
        this.hasBluetooth = builder.hasBluetooth;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Computer Specifications:\n");
        sb.append("  CPU: ").append(cpu).append("\n");
        sb.append("  RAM: ").append(ram).append("\n");
        sb.append("  Storage: ").append(storage != null ? storage : "Not specified").append("\n");
        sb.append("  Graphics Card: ").append(graphicsCard != null ? graphicsCard : "Integrated").append("\n");
        sb.append("  Power Supply: ").append(powerSupply != null ? powerSupply : "Standard").append("\n");
        sb.append("  Motherboard: ").append(motherboard != null ? motherboard : "Standard").append("\n");
        sb.append("  Cooling System: ").append(coolingSystem != null ? coolingSystem : "Air Cooling").append("\n");
        sb.append("  WiFi: ").append(hasWifi ? "Yes" : "No").append("\n");
        sb.append("  Bluetooth: ").append(hasBluetooth ? "Yes" : "No").append("\n");
        return sb.toString();
    }
    
    /**
     * Static Builder Class (Fluent Builder Pattern)
     * Effective Java에서 권장하는 방식
     */
    public static class Builder {
        // 필수 파라미터
        private final String cpu;
        private final String ram;
        
        // 선택적 파라미터 - 기본값으로 초기화
        private String storage = "500GB HDD";
        private String graphicsCard = null;
        private String powerSupply = "500W";
        private String motherboard = "ATX Standard";
        private String coolingSystem = "Air Cooling";
        private boolean hasWifi = false;
        private boolean hasBluetooth = false;
        
        /**
         * Builder 생성자 - 필수 파라미터만 받음
         */
        public Builder(String cpu, String ram) {
            this.cpu = cpu;
            this.ram = ram;
        }
        
        public Builder storage(String storage) {
            this.storage = storage;
            return this;
        }
        
        public Builder graphicsCard(String graphicsCard) {
            this.graphicsCard = graphicsCard;
            return this;
        }
        
        public Builder powerSupply(String powerSupply) {
            this.powerSupply = powerSupply;
            return this;
        }
        
        public Builder motherboard(String motherboard) {
            this.motherboard = motherboard;
            return this;
        }
        
        public Builder coolingSystem(String coolingSystem) {
            this.coolingSystem = coolingSystem;
            return this;
        }
        
        public Builder hasWifi(boolean hasWifi) {
            this.hasWifi = hasWifi;
            return this;
        }
        
        public Builder hasBluetooth(boolean hasBluetooth) {
            this.hasBluetooth = hasBluetooth;
            return this;
        }
        
        /**
         * 최종적으로 Computer 객체를 생성
         */
        public Computer build() {
            // 여기서 유효성 검증도 가능
            if (cpu == null || cpu.isEmpty()) {
                throw new IllegalStateException("CPU is required");
            }
            if (ram == null || ram.isEmpty()) {
                throw new IllegalStateException("RAM is required");
            }
            return new Computer(this);
        }
    }
}
