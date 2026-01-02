public class Product {
    private  int id;
    private boolean usable;
    private String category;
    private int price;

    public Product(int id, boolean usable, String category, int price) {
        this.id = id;
        this.usable = usable;
        this.category = category;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public boolean isUsable() {
        return usable;
    }

    public String getCategory() {
        return category;
    }

    public int getPrice() {
        return price;
    }

    
}
