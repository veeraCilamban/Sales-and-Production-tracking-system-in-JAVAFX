package application;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class TPInvoice {
    private SimpleStringProperty invNumber;
    private SimpleStringProperty customerName;
    private SimpleDoubleProperty totalAmount;
    private SimpleStringProperty date;
    private SimpleIntegerProperty productSold;

    public TPInvoice(String invNumber, String customerName, Double totalAmount, String date,  Integer productSold) {
        this.invNumber = new SimpleStringProperty(invNumber);
        this.customerName = new SimpleStringProperty(customerName);
        this.totalAmount = new SimpleDoubleProperty(totalAmount);
        this.date = new SimpleStringProperty(date);
        this.productSold = new SimpleIntegerProperty(productSold);
    }

    public String getInvNumber() {
        return invNumber.get();
    }

    public SimpleStringProperty invNumberProperty() {
        return invNumber;
    }

    public void setInvNumber(String invNumber) {
        this.invNumber.set(invNumber);
    }

    public String getCustomerName() {
        return customerName.get();
    }

    public SimpleStringProperty customerNameProperty() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName.set(customerName);
    }

    public double getTotalAmount() {
        return totalAmount.get();
    }

    public SimpleDoubleProperty totalAmountProperty() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount.set(totalAmount);
    }

        public String getDate() {
        return date.get();
    }

    public SimpleStringProperty dateProperty() {
        return date;
    }

    public void setDate(String date) {
        this.date.set(date);
    }



    public int getProductSold() {
        return productSold.get();
    }

    public SimpleIntegerProperty productSoldProperty() {
        return productSold;
    }

    public void setProductSold(int productSold) {
        this.productSold.set(productSold);
    }
}
