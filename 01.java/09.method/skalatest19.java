package skalajava;

public class skalatest19 {
    public static void main(String[] args) {
        Variable variable = new Variable();
        variable.a = 3;
        increase(variable);
        System.out.println(variable.a);
    }

    static void increase(Variable variable){
        variable.a++;
        System.out.println(variable.a);
    }
}
