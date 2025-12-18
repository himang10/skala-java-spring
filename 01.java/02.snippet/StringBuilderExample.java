public class StringBuilderExample {
    public static void main(String[] args) {
        String name = "스칼라";
        int age = 25;

        StringBuilder sb1 = new StringBuilder();
        sb1.append("이름: ").append(name)
        .append(", 나이: ").append(age);

        System.out.println(sb1.toString());


        StringBuilder sb2 = new StringBuilder();
        sb2.append(String.format("%-10s", "스칼라"))  // 이름: 왼쪽 정렬
        .append(String.format("%5d", 25));        // 나이: 오른쪽 정렬

        System.out.println(sb2.toString());    // 출력: 스칼라  25


        String[] stocks = {"SKALA", "EDU", "AI"};
        int[] prices = {12300, 9850, 23000};

        StringBuilder sb3 = new StringBuilder();
        sb3.append(String.format("%-10s %10s\n", "종목", "현재가"));
        for (int i = 0; i < stocks.length; i++) {
            sb3.append(String.format("%-10s %,10d\n", stocks[i], prices[i]));
        }
        System.out.println(sb3.toString());

    }
}
