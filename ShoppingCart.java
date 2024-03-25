import java.util.ArrayList;

// ShoppingCart Class
public class ShoppingCart {
    public ArrayList<Product> products;
    private int total;

    // Parameterized Constructor
    public ShoppingCart(ArrayList<Product> products, int total) {
        this.products = products;
        this.total = total;
    }


    // Getters and Setters
    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }


}
