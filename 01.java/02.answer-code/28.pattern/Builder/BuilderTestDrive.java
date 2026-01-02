/**
 * Client
 * Builder 패턴을 사용하는 클라이언트 코드
 */
public class BuilderTestDrive {
    
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("Builder Pattern Demo");
        System.out.println("========================================\n");
        
        // 1. 게이밍 컴퓨터 - 모든 옵션 지정
        System.out.println("===== 게이밍 컴퓨터 =====\n");
        
        Computer gamingComputer = new Computer.Builder("Intel Core i9-13900K", "64GB DDR5")
                .storage("2TB NVMe SSD")
                .graphicsCard("NVIDIA RTX 4090")
                .powerSupply("1000W 80+ Gold")
                .motherboard("ASUS ROG Maximus Z790")
                .coolingSystem("Liquid Cooling")
                .hasWifi(true)
                .hasBluetooth(true)
                .build();
        
        System.out.println(gamingComputer);
        
        // 2. 사무용 컴퓨터 - 필수 옵션만 지정
        System.out.println("\n===== 사무용 컴퓨터 =====\n");
        
        Computer officeComputer = new Computer.Builder("Intel Core i5-13400", "16GB DDR4")
                .storage("512GB SSD")
                .hasWifi(true)
                .build();
        
        System.out.println(officeComputer);
        
        // 3. 개발자용 컴퓨터 - 선택적 옵션 조합
        System.out.println("\n===== 개발자용 컴퓨터 =====\n");
        
        Computer devComputer = new Computer.Builder("Intel Core i7-13700K", "32GB DDR5")
                .storage("1TB NVMe SSD")
                .graphicsCard("NVIDIA RTX 4070")
                .coolingSystem("Air Cooling Premium")
                .hasWifi(true)
                .hasBluetooth(true)
                .build();
        
        System.out.println(devComputer);
        
        // 4. 최소 사양 컴퓨터 - 필수만 지정 (나머지는 기본값 사용)
        System.out.println("\n===== 최소 사양 컴퓨터 =====\n");
        
        Computer minimalComputer = new Computer.Builder("Intel Core i3-13100", "8GB DDR4")
                .build();
        
        System.out.println(minimalComputer);
    }
}
