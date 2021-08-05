package application;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class InvoiceList {
    private SimpleStringProperty invoiceNumber;
    private SimpleStringProperty customerName;
    private SimpleDoubleProperty totalAmount;
    private SimpleStringProperty date;

    public InvoiceList(String invoiceNumber, String customerName, Double totalAmount, String date) {
        this.invoiceNumber = new SimpleStringProperty(invoiceNumber);
        this.customerName = new SimpleStringProperty(customerName);
        this.totalAmount = new SimpleDoubleProperty(totalAmount);
        this.date = new SimpleStringProperty(date);
    }

    public String getInvoiceNumber() {
        return invoiceNumber.get();
    }

    public SimpleStringProperty invoiceNumberProperty() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber.set(invoiceNumber);
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


}
