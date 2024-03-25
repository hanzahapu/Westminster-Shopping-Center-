// Class Electronics extends Product
class Electronics extends Product{
    public String brand;
    public int warrantyPeriods;

    // Default Constructor
    Electronics(){
        super();

    }

    // Parameterized Constructor
    public Electronics(String brand, int warrantyPeriods,String productName, int noOfItemAvailable, double price, String productId) {
        super(productName,noOfItemAvailable,price,productId);
        this.brand = brand;
        this.warrantyPeriods = warrantyPeriods;
    }


    // Overriding toString Method
    @Override
    public String toString() {
        return "Electronics{" +
                "productId='" + productId + '\'' +
                ", brand='" + brand + '\'' +
                ", warrantyPeriods='" + warrantyPeriods + '\'' +
                ", productName='" + productName + '\'' +
                ", noOfItemAvailable=" + noOfItemAvailable +
                ", price=" + price +
                '}';
    }



    // Getters and Setters

    public int getWarrantyPeriods() {
        return warrantyPeriods;
    }

    public void setWarrantyPeriods(int warrantyPeriods) {
        this.warrantyPeriods = warrantyPeriods;
    }

    @Override
    public String getProductId() {

        return productId != null ? productId : "";
    }

    @Override
    public void setProductId(String productId) {
        this.productId=productId;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public void setPrice(double price) {
        this.price=price;
    }



    @Override
    public String getProductName() {
        return productName;
    }

    @Override
    public void setProductName(String productName) {
        this.productName=productName;
    }



    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }


    @Override
    public void setNoOfItemAvailable(int noOfItemAvailable) {
        this.noOfItemAvailable=noOfItemAvailable;
    }

    @Override
    public int getNoOfItemAvailable() {
        return noOfItemAvailable;
    }


}
