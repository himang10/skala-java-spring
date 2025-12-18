public class Main {
    public static void main(String[] args) {
        // 전자제품: 노트북
        Laptop laptop = new Laptop("울트라랩 14", 1_200_000, 1, 24, 16);
        laptop.extendWarranty(12);  // 보증 12개월 연장 -> 총 36개월
        laptop.upgradeRAM(16);      // RAM 16GB 추가 -> 32GB

        // 의류: 티셔츠
        TShirt tee = new TShirt("베이직 티셔츠", 25_000, 3, "M", "Cotton");
        tee.applyDiscount(10);      // 10% 할인 -> 개당 22,500원
        tee.changeSize("L");        // 사이즈 변경

        // 주문 생성 및 상품 추가
        Order order = new Order();
        order.addProduct(laptop);
        order.addProduct(tee);

        // 결과 출력
        order.printSummary();
        // 참고: laptop, tee 각각의 toString에 상세 정보가 표시됩니다.
    }
}
