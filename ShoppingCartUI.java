import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

// Class ShoppingCartUI extends JFrame
public class ShoppingCartUI extends JFrame {
    private DefaultTableModel table;

    private JTextField finalTotalField;

    private JTextField totalField;

    private JTextField firstPurchaseDiscount;

    private JTextField categoryDiscount;

    // ShoppingCart object to manage the products in the cart
    private ShoppingCart shoppingCart;
    private int noOfElecs=0;
    private int noOfClothings=0;


    // Total cost of items in the cart
    private double total=0;


    // Constructor for ShoppingCartUI
    public ShoppingCartUI(ShoppingCart shoppingCart){
        this.shoppingCart=shoppingCart;


        // Setting up the UI window
        setTitle("Shopping Cart");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(1200,600);
        setLayout(new BorderLayout());


        // Setting up the table to display product details
        String[] columnNames={"Product","Quantity","Price"};
        table=new DefaultTableModel(columnNames,0);
        JTable jTable=new JTable(table);

        JScrollPane scrollPane=new JScrollPane(jTable);
        add(scrollPane,BorderLayout.CENTER);


        // Setting up the panel
        JPanel totalPanel = new JPanel();
        totalPanel.setLayout(new GridLayout(8, 2));


        // Adding components to the total panel
        categoryDiscount = new JTextField();
        totalPanel.add(new JLabel("Total : "));

        totalField=new JTextField();
        totalPanel.add(totalField);

        firstPurchaseDiscount = new JTextField();
        totalPanel.add(new JLabel("First Purchase Discount (10%) : "));
        totalPanel.add(firstPurchaseDiscount);

        totalPanel.add(new JLabel("Three Items in Same Category Discount (20%) : "));
        totalPanel.add(categoryDiscount);

        finalTotalField = new JTextField();
        totalPanel.add(new JLabel("Final Total : "));

        totalPanel.add(finalTotalField);
        add(totalPanel, BorderLayout.SOUTH);


        // Updating the product table
        updateProductTable(shoppingCart);
        calculateTotals();
    }




    // Method to create row data
    public Object[] electronicsInfo(Electronics p){
        Object [] row={p.getProductId()+"\n"+p.getProductName()+"\n"+p.getBrand()+", "+p.getWarrantyPeriods(),p.getNoOfItemAvailable(),p.getPrice()};
        noOfElecs++;
        return row;
    }

    // Calculate total Method
    public void calculateTotals(){
        double price=0;
        int qty=0;
        double fPurchase=0;
        double catDisc=0;
        for (int i=0; i<table.getRowCount(); i++){
            price=Double.parseDouble(table.getValueAt(i,2).toString());
            qty=Integer.parseInt(table.getValueAt(i,1).toString());
            total+=price*qty;
        }

        double discounts=0;

        if(qty==1) {
            fPurchase=(total*0.1);
        }


        if(noOfClothings>3 ){
            catDisc=(total*0.2);
        }else if(noOfElecs>3){
            catDisc=(total*0.2);
        }


        firstPurchaseDiscount.setText("-"+fPurchase);
        double finalTotal=total- catDisc-fPurchase;

        totalField.setText(String.valueOf(total));
        categoryDiscount.setText("-"+catDisc);
        finalTotalField.setText(String.valueOf(finalTotal));
    }


    // Method to create row data for clothing
    public Object[] clothingInfor(Clothing p){
        Object [] row={p.getProductId()+"\n"+p.getProductName()+"\n"+p.getSize()+", "+p.getColour(),p.getNoOfItemAvailable(),p.getPrice()};
        noOfClothings++;

        return row;
    }


    // Method to update the product table
    public void updateProductTable(ShoppingCart shoppingCart){
        ArrayList<Product> productsList=shoppingCart.getProducts();
        if (productsList != null) {
            table.setRowCount(0);


            for (Product p : productsList) {
                if ((p.toString().substring(0, p.toString().indexOf('{')).trim()).equals("Clothing")) {

                    Object[] row = clothingInfor((Clothing) p);
                    table.addRow(row);


                } else if ((p.toString().substring(0, p.toString().indexOf('{')).trim()).equals("Electronics")) {

                    Object[] row = electronicsInfo((Electronics) p);
                    table.addRow(row);
                }
            }
        }
    }

}
