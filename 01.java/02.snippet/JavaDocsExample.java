/**
 * 간단한 계산기 클래스.
 * <p>
 * 두 개의 정수에 대해 덧셈과 뺄셈 기능을 제공합니다.
 * </p>
 *
 * @author skala
 * @version 1.0
 * @since 2025-08-13
 */
public class JavaDocsExample {

    /**
     * 마지막 계산 결과를 저장합니다.
     */
    private int lastResult;

    /**
     * 기본 생성자.
     * 초기 lastResult 값은 0입니다.
     */
    public JavaDocsExample() {
        this.lastResult = 0;
    }

    /**
     * 두 정수를 더합니다.
     *
     * @param a 첫 번째 정수
     * @param b 두 번째 정수
     * @return 덧셈 결과
     */
    public int add(int a, int b) {
        lastResult = a + b;
        return lastResult;
    }

    /**
     * 두 정수를 뺍니다.
     *
     * @param a 첫 번째 정수
     * @param b 두 번째 정수
     * @return 뺄셈 결과 (a - b)
     */
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
