/**
 * Concrete Product
 * 페퍼로니 피자 구현체
 */
public class PepperoniPizza implements Pizza {
    private String name = "Pepperoni Pizza";
    
    @Override
    public void prepare() {
        System.out.println("Preparing " + name);
        System.out.println("Adding pepperoni topping...");
    }
    
    @Override
    public void bake() {
        System.out.println("Baking " + name + " at 350 degrees for 25 minutes");
    }
    
    @Override
    public void cut() {
        System.out.println("Cutting " + name + " into diagonal slices");
    }
    
    @Override
    public void box() {
        System.out.println("Placing " + name + " in official PizzaStore box");
    }
    
    @Override
    public String getName() {
        return name;
    }
}
