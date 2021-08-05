package application;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ProductMgt {
    private SimpleIntegerProperty productID;
    private SimpleStringProperty productName;
    private SimpleDoubleProperty productPrice;
    private SimpleIntegerProperty gst;
    private SimpleIntegerProperty stocks;
    private SimpleStringProperty date;



    public ProductMgt(int productID, String productName, Double productPrice, int gst, int stocks, String date) {
        this.productID = new SimpleIntegerProperty(productID);
        this.productName = new SimpleStringProperty(productName);
        this.productPrice = new SimpleDoubleProperty(productPrice);
        this.gst = new SimpleIntegerProperty(gst);
        this.stocks = new SimpleIntegerProperty(stocks);
        this.date = new SimpleStringProperty(date);
    }

    public int getProductID() {
        return productID.get();
    }

    public void setProductID(int productID) {
        this.productID = new SimpleIntegerProperty(productID);
    }



    public String getProductName() {
        return productName.get();
    }

    public void setProductName(String productName) {
        this.productName = new SimpleStringProperty(productName);
    }



    public double getProductPrice() {
        return productPrice.get();
    }

    public void setProductPrice(Double productPrice) {
        this.productPrice = new SimpleDoubleProperty(productPrice);
    }


    public int getGst() {
        return gst.get();
    }



    public void setGst(int gst) {
        this.gst = new SimpleIntegerProperty(gst);
    }

    public int getStocks() {
        return stocks.get();
    }


    public void setStocks(int stocks) {
        this.stocks = new SimpleIntegerProperty(stocks);
    }

    public String getDate() {
        return date.get();
    }

    public void setDate(String date) {
        this.date = new SimpleStringProperty(date);
    }
}
