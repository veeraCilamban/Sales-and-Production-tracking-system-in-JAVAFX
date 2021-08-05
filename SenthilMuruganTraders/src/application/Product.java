package application;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Product {
    private SimpleStringProperty productName;
    private SimpleIntegerProperty productID;
    private SimpleDoubleProperty productPrice;
    private SimpleDoubleProperty qty;
    private SimpleIntegerProperty productGSTPercentage;
    private SimpleDoubleProperty productGST;
    private SimpleDoubleProperty amount;

    public Product(String productName,int productID,Double productPrice,Double qty,int productGSTPercentage,Double productGST,Double amount){
        this.productName = new SimpleStringProperty(productName);
        this.productID = new SimpleIntegerProperty(productID);
        this.productPrice = new SimpleDoubleProperty(productPrice);
        this.qty = new SimpleDoubleProperty(qty);
        this.productGSTPercentage = new SimpleIntegerProperty(productGSTPercentage);
        this.productGST = new SimpleDoubleProperty(productGST);
        this.amount = new SimpleDoubleProperty(amount);

    }

    public String getProductName() {
        return productName.get();
    }

    public void setProductName(String productName) {
        this.productName = new SimpleStringProperty(productName);
    }

    public int getProductID() {
        return productID.get();
    }

    public void setProductID(int productID) {
        this.productID = new SimpleIntegerProperty(productID);
    }

    public Double getProductPrice() {
        return productPrice.get();
    }

    public void setProductPrice(Double productPrice) {
        this.productPrice = new SimpleDoubleProperty(productPrice);
    }

    public Double getQty() {
        return qty.get();
    }

    public void setQty(int qty) {
        this.qty = new SimpleDoubleProperty(qty);
    }

    public Double getProductGST() {
        return productGST.get();
    }

    public void setProductGST(int productGST) {
        this.productGST = new SimpleDoubleProperty(productGST);
    }

    public int getProductGSTPercentage() {
        return productGSTPercentage.get();
    }

    public void setProductGSTPercentage(Integer productGSTPercentage) {
        this.productGSTPercentage = new SimpleIntegerProperty(productGSTPercentage);
    }

    public Double getAmount() {
        return amount.get();
    }

    public void setAmount(Double amount) {
        this.amount = new SimpleDoubleProperty(amount);
    }
}
