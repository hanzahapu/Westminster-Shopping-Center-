// Products Abstract class
abstract class Product {
    public String productName;
    public int noOfItemAvailable;
    public double price;
    public String productId;

    // Default Constructor
    Product(){

    }

    // Overriding toString Method
    @Override
    public String toString() {
        return "Product{" +
                "productName='" + productName + '\'' +
                ", noOfItemAvailable=" + noOfItemAvailable +
                ", price=" + price +
                ", productId='" + productId + '\'' +
                '}';
    }

    // Parameterized Constructor
    public Product(String productName, int noOfItemAvailable, double price, String productId) {
        this.productName = productName;
        this.noOfItemAvailable = noOfItemAvailable;
        this.price = price;
        this.productId = productId;
    }


    // Abstract methods to Getters and Setters
    public abstract String getProductId() ;

    public abstract void setProductId(String productId) ;

    public abstract int getNoOfItemAvailable() ;

    public abstract void setNoOfItemAvailable(int noOfItemAvailable);


    public abstract String getProductName();

    public abstract void setProductName(String productName);


    public abstract double getPrice();

    public abstract void setPrice(double price);
}
