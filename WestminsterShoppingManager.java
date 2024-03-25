import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Class WestminsterShoppingManager implements ShoppingManager interface
public class WestminsterShoppingManager implements ShoppingManager{


    // ArrayList to store products
    static ArrayList<Product> products=new ArrayList<>();
    static int index=0;


    // Method to add a new product to the list
    public static void saveProduct(){
        System.out.println("Select the type : ");
        System.out.println("1. Electronics\n2. Clothes");
        Scanner input=new Scanner(System.in);


        // Loop to handle product addition
        while (true) {
            if(index==50){
                System.out.println("Store is Full");
                break;
            }
            int type=0;
            try{
                type=input.nextInt();
            }catch (Exception e){
                System.out.println("Enter 1 or 2 !");
                input.nextLine();
                type=input.nextInt();
            }
            input.nextLine();

            // Handling electronics product addition
            if(type==1){
                System.out.println("Enter Brand :");
                String brand=input.nextLine();
                System.out.println("Enter Warranty Period : ");
                int warrantyPeriods=0;
                try{
                    warrantyPeriods=input.nextInt();
                }catch (Exception e){
                    System.out.println("Enter a valid integer value.");
                    input.nextLine();
                    warrantyPeriods=input.nextInt();
                }
                input.nextLine();
                System.out.println("Enter Product Name : ");
                String productName=input.nextLine();
                System.out.println("Enter No of Items available : ");
                int noOfItemAvailable=0;
                try{
                    noOfItemAvailable=input.nextInt();
                }catch (Exception e){
                    System.out.println("Enter a valid integer value.");
                    input.nextLine();
                    noOfItemAvailable=input.nextInt();
                }

                System.out.println("Enter the price : ");
                double price;
                try{
                    price=input.nextDouble();
                }catch (Exception e){
                    System.out.println("Enter a valid decimal value.");
                    input.nextLine();
                    price=input.nextDouble();
                }
                input.nextLine();
                System.out.println("Enter the product Id :");
                String productId=input.nextLine();
                Product e=new Electronics(brand,warrantyPeriods,productName,noOfItemAvailable,price,productId);
                products.add(e);
                index++;
                break;

                //  Handling Clothing product addition
            }else if(type==2){
                System.out.println("Enter Product Name : ");
                String productName=input.nextLine();
                System.out.println("Enter No of Items available : ");
                int noOfItemAvailable=0;
                try{
                    noOfItemAvailable=input.nextInt();
                }catch (Exception e){
                    System.out.println("Enter a valid integer value.");
                    input.nextLine();
                    noOfItemAvailable=input.nextInt();
                }
                System.out.println("Enter the price : ");
                double price;
                try{
                    price=input.nextDouble();
                }catch (Exception e){
                    System.out.println("Enter a valid decimal value.");
                    input.nextLine();
                    price=input.nextDouble();
                }
                input.nextLine();
                System.out.println("Enter the product Id :");
                String productId=input.nextLine();
                System.out.println("Size : ");
                String size=input.nextLine();
                System.out.println("Color : ");
                String colour=input.nextLine();

                Product c=new Clothing(size,colour,productName,noOfItemAvailable,price,productId);
                products.add(c);
                index++;

                break;
            }else{
                System.out.println("Enter 1 or 2 !");
            }

        }
    }


    // Method to remove a product
    public static void removeProduct(String productId){

        l1:for (int i = 0; i < products.size(); i++) {
            Product p=products.get(i);
            if(p.getProductId().equals(productId)){
                products.remove(i);
                System.out.println("Removed.");
                break l1;
            }
        }
        index--;
    }


    // Method to print the list of products
    public static int printProductsList(){
        if(index==0){
            System.out.println("No Items in the list.");
            return 0;
        }
        Collections.sort(products,Comparator.comparing(Product::getProductId,Comparator.nullsLast(String::compareTo)));
        for (Product p:
                products) {
            System.out.println(p);
        }
        return 0;
    }


    // Saving the list of products to a file
    public static void saveToFile(){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("products.txt"))) {
            for (Product product : products) {
                writer.write(product.toString());
                writer.newLine();
            }
            System.out.println("Saved to file Successfully. ");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // fetching a Clothing object from a string
    public static Clothing fetchCloth(String line){

        String regex = "Clothing\\{productId='(.+)', size=(.+), colour='(.+)', productName='(.+)', noOfItemAvailable=(\\d+), price=(\\d+\\.\\d+)\\}";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);

        Clothing c=new Clothing();
        if(matcher.matches()){
            c.setPrice(Double.parseDouble(matcher.group(6)));
            c.setColour(matcher.group(3));
            c.setSize(matcher.group(2));
            c.setProductId(matcher.group(1));
            c.setProductName(matcher.group(4));
            c.setNoOfItemAvailable(Integer.parseInt(matcher.group(5)));
        }else{
            return null;
        }

        return c;
    }


    // fetching an Electronics object from a string
    public static Electronics fetchElect(String line){
        String regex = "Electronics\\{productId='(.+)', brand='(.+)', warrantyPeriods='(.+)', productName='(.+)', noOfItemAvailable=(\\d+), price=(\\d+\\.\\d+)\\}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        Electronics e=new Electronics();

        if(matcher.matches()){
            e.setBrand(matcher.group(2));
            e.setWarrantyPeriods(Integer.parseInt(matcher.group(3)));
            e.setPrice(Double.parseDouble(matcher.group(6)));
            e.setProductId(matcher.group(1));
            e.setProductName(matcher.group(4));
            e.setNoOfItemAvailable(Integer.parseInt(matcher.group(5)));

        }else{
            return null;
        }
        return e;
    }

    // reading back products from a file
    public static void readBackProducts(){
        try (BufferedReader reader = new BufferedReader(new FileReader("products.txt"))) {
            String line;
            index=-1;
            while ((line = reader.readLine()) != null) {

                int braceIndex = line.indexOf('{');
                if ((line.substring(0, braceIndex).trim()).equals("Electronics")) {
                    Product p=fetchElect(line);
                    products.add(p);
                    System.out.println(line);
                }else if((line.substring(0, braceIndex).trim()).equals("Clothing")) {
                    Product p=fetchCloth(line);
                    products.add(p);
                    System.out.println(line);
                }

            }
            System.out.println("All records reloaded.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    // Main method
    public static void main(String[] args) {
        while(true){
            System.out.println("\n|| Select an option || ");
            System.out.println("\n1 : Add a new product");
            System.out.println("2 : Delete a product");
            System.out.println("3 : Print the list of the products");
            System.out.println("4 : Save in a file.");
            System.out.println("5 : Read back all the information.");
            Scanner input=new Scanner(System.in);
            int choice=0;
            try{
                choice=input.nextInt();
            }catch (Exception e){
                System.out.println("Invalid Input! Enter a valid number from the list.");
                input.nextLine();
                choice=input.nextInt();
            }
            switch (choice){
                case 1:
                    saveProduct();
                    break;
                case 2:
                    input.nextLine();
                    System.out.println("Enter product ID : ");
                    String id=input.nextLine();
                    removeProduct(id);
                    break;
                case 3:
                    printProductsList();
                    break;
                case 4:
                    saveToFile();
                    break;
                case 5:
                    readBackProducts();
                    break;
                default:
                    System.out.println("Invalid Input. Try Again");
                    break;
            }
        }

    }

}
