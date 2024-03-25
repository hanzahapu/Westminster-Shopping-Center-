import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


// Class WestministerShoppingCenterUI extends JFrame
public class WestministerShoppingCenterUI extends JFrame {


    // UI components and fields
    private JPanel productDetails;
    private JComboBox<String> productType;
    private JButton addToCart;
    private JButton viewCart;
    private JTable productTable;

    // Table model for displaying product information
    private DefaultTableModel tableModel;


    // ArrayList to store product data for the table
    private ArrayList<TableData> productsTable=new ArrayList<>();

    // Counters
    private int electQty=0;
    private int clothQty=0;


    private ShoppingCart sC=new ShoppingCart(new ArrayList<>(),0);


    private ShoppingCartUI sCUI =null;

    // Constructor for WestministerShoppingCenterUI
    public WestministerShoppingCenterUI(){
        setTitle("Westminster Shopping Center");
        setSize(1200,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        // Setup for product type selection and its action listener
        productType=new JComboBox<>(new String[]{"Electronics","Clothing"});
        productType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                updateProductTable();
            }

            // UpdateProductTable Method
            private void updateProductTable(){
                productsTable=fetchProducts(productType.getSelectedItem().toString());

                tableModel.setRowCount(0);
                for (TableData p:
                        productsTable) {
                    Object [] row={p.getProductId(),p.getName(),p.getCategory(),p.getPrice(),p.getInfo()};
                    tableModel.addRow(row);
                }

                highLightLowAvailableProducts();


            }

        });


        // Table model setup
        tableModel=new DefaultTableModel(new Object[]{"ProductId","Name","Category","Price","Info"},0);
        productTable = new JTable(tableModel);

        // Setup for product details panel
        productDetails=new JPanel();
        productDetails.setBorder(BorderFactory.createTitledBorder("Selected Product - Details"));
        productDetails.setPreferredSize(new Dimension(800, 250));

        // Add to cart button setup
        addToCart=new JButton("Add to Cart");
        addToCart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addToCart();
            }
        });


        // Layout setup
        productDetails.setLayout(new BorderLayout());
        productDetails.add(addToCart, BorderLayout.SOUTH);

        // View cart button setup
        viewCart=new JButton("Shopping Cart");
        viewCart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(sCUI==null){
                    sCUI=new ShoppingCartUI(sC);
                    sCUI.setVisible(true);
                }else{
                    sCUI.setVisible(true);
                    sCUI.updateProductTable(sC);
                    sCUI.calculateTotals();
                }

            }
        });


        // Main layout setup
        setLayout(new BorderLayout());
        JPanel topPanel=new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.add(productType);
        topPanel.add(viewCart);
        add(topPanel,BorderLayout.NORTH);
        add(new JScrollPane(productTable),BorderLayout.CENTER);
        add(productDetails,BorderLayout.SOUTH);
        add(addToCart,BorderLayout.EAST);
        productDetails.add(addToCart, BorderLayout.SOUTH);


        // List selection listener for product table
        productTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    updateProductDetailsPanel();
                }
            }
        });
    }



    // Highlighting low available products Renderer Class
    private class HighlightRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            c.setBackground(Color.RED);
            return c;
        }
    }

    // UpdateProductDetailsPanel Method
    private void updateProductDetailsPanel() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow != -1) {
            TableData selectedProduct = productsTable.get(selectedRow);
            displayProductDetails(selectedProduct);
        }
    }

    // highlight Low Available Products Method
    private void highLightLowAvailableProducts() {
        for (int i = 0; i < productsTable.size(); i++) {
            int amnt = productsTable.get(i).getQuantity();
            if (amnt < 3) {
                productTable.setRowSelectionInterval(i, i);
                productTable.getColumnModel().getColumn(0).setCellRenderer(new HighlightRenderer());
            }
        }
    }





    // Add to cart method
    private void addToCart(){
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow != -1) {
            TableData selectedProduct = productsTable.get(selectedRow);
            if(searchProduct(selectedProduct.getProductId())==null){
                if(selectedProduct.getCategory().equals("Electronics")){
                    electQty++;
                    Electronics e=new Electronics();
                    e.setPrice(Double.parseDouble(selectedProduct.getPrice()));
                    e.setNoOfItemAvailable(1);
                    e.setProductId(selectedProduct.getProductId());
                    e.setProductName(selectedProduct.getName());
                    String warr=selectedProduct.getInfo().split(", ")[0];
                    String brand=selectedProduct.getInfo().split(", ")[1];
                    e.setWarrantyPeriods(Integer.parseInt(warr));
                    e.setBrand(brand);
                    sC.products.add(e);

                }else if(selectedProduct.getCategory().equals("Clothing")){
                    clothQty++;
                    Clothing e=new Clothing();
                    e.setPrice(Double.parseDouble(selectedProduct.getPrice()));
                    e.setNoOfItemAvailable(1);
                    e.setProductId(selectedProduct.getProductId());
                    e.setProductName(selectedProduct.getName());
                    String size=selectedProduct.getInfo().split(", ")[1];
                    String col=selectedProduct.getInfo().split(", ")[0];
                    e.setSize(size);
                    e.setColour(col);
                    sC.products.add(e);
                }

            }else{
                for (int i = 0; i < sC.products.size(); i++) {
                    Product p=sC.products.get(i);
                    if(p.getProductId().equals(selectedProduct.getProductId())){
                        p.setNoOfItemAvailable(p.getNoOfItemAvailable()+1);
                    }
                }
            }
        }
    }

    // Display the product details method
    private void displayProductDetails(TableData selectedProduct) {
        StringBuilder details = new StringBuilder();
        details.append("<html><b>Category:</b> ").append(selectedProduct.getCategory()).append("<br>");
        details.append("<b>Name:</b> ").append(selectedProduct.getName()).append("<br>");
        details.append("<b>Price:</b> ").append(selectedProduct.getPrice()).append("<br>");
        details.append("<b>Info:</b> ").append(selectedProduct.getInfo()).append("</html>");

        //productDetails.setLayout(new BorderLayout());
        productDetails.removeAll();
        JLabel detailsLabel = new JLabel(details.toString());
        detailsLabel.setVerticalAlignment(SwingConstants.TOP);
        productDetails.add(detailsLabel, BorderLayout.CENTER);
        productDetails.add(addToCart, BorderLayout.SOUTH);
        productDetails.revalidate();
        productDetails.repaint();
    }

    // Search Product Method
    public Product searchProduct(String productId){

        l1:for (int i = 0; i < sC.products.size(); i++) {
            Product p=sC.products.get(i);
            if(p.getProductId().equals(productId)){
                return p;
            }
        }
        return null;
    }



    // Method fetchProducts
    private ArrayList<TableData> fetchProducts(String type){
        ArrayList<TableData> products=new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("products.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {

                int braceIndex = line.indexOf('{');
                if ((line.substring(0, braceIndex).trim()).equals(type)) {
                    if(type.equals("Electronics")){
                        TableData p=electronicsInfo(line);
                        products.add(p);
                    }else if(type.equals("Clothing")){
                        TableData p=clothingInfor(line);
                        products.add(p);
                    }
                }

            }
            System.out.println("All records reloaded.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return products;

    }



    // Method clothing Information
    public static TableData clothingInfor(String line){

        String regex = "Clothing\\{productId='(.+)', size=(.+), colour='(.+)', productName='(.+)', noOfItemAvailable=(\\d+), price=(\\d+\\.\\d+)\\}";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);

        TableData t=new TableData();
        if(matcher.matches()){

            // Extract data from the regex groups
            t.setCategory("Clothing");
            t.setInfo(matcher.group(3)+", "+matcher.group(2));
            t.setPrice(matcher.group(6));
            t.setProductId(matcher.group(1));
            t.setName(matcher.group(4));
            t.setQuantity(Integer.parseInt(matcher.group(5)));
        }else{
            return null;
        }

        return t;
    }

    // Method electronics Information
    public static TableData electronicsInfo(String line){
        String regex = "Electronics\\{productId='(.+)', brand='(.+)', warrantyPeriods='(.+)', productName='(.+)', noOfItemAvailable=(\\d+), price=(\\d+\\.\\d+)\\}";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);

        TableData t=new TableData();
        if(matcher.matches()){

            // Extract data from the regex groups
            t.setCategory("Electronics");
            t.setInfo(matcher.group(3)+", "+matcher.group(2));
            t.setPrice(matcher.group(6));
            t.setProductId(matcher.group(1));
            t.setName(matcher.group(4));
            t.setQuantity(Integer.parseInt(matcher.group(5)));
        }else{
            return null;
        }
        return t;
    }


    // Main method to run the application
    public static void main(String [] args){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new WestministerShoppingCenterUI().setVisible(true);
            }
        });
    }




}
