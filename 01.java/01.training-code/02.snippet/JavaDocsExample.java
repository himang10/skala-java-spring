
public class JavaDocsExample {

    private int lastResult;

    public JavaDocsExample() {
        this.lastResult = 0;
    }


    public int add(int a, int b) {
        lastResult = a + b;
        return lastResult;
    }

    public int subtract(int a, int b) {
        lastResult = a - b;
        return lastResult;
    }

    /**
     * 마지막 계산 결과를 반환합니다.
     *
     * @return 마지막 계산 결과
     */
    public int getLastResult() {
        return lastResult;
    }
}
