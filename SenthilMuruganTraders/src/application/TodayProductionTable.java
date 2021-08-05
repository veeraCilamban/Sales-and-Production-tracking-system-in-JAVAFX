package application;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class TodayProductionTable {
    private SimpleIntegerProperty tpID;
    private SimpleIntegerProperty tpStocks;
    private SimpleStringProperty tpProductName;

    public int getTpID() {
        return tpID.get();
    }

    public SimpleIntegerProperty tpIDProperty() {
        return tpID;
    }

    public void setTpID(int tpID) {
        this.tpID.set(tpID);
    }

    public int getTpStocks() {
        return tpStocks.get();
    }

    public SimpleIntegerProperty tpStocksProperty() {
        return tpStocks;
    }

    public void setTpStocks(int tpStocks) {
        this.tpStocks.set(tpStocks);
    }

    public String getTpProductName() {
        return tpProductName.get();
    }

    public SimpleStringProperty tpProductNameProperty() {
        return tpProductName;
    }

    public void setTpProductName(String tpProductName) {
        this.tpProductName.set(tpProductName);
    }

    public TodayProductionTable(Integer tpID, Integer tpStocks, String tpProductName) {
        this.tpID = new SimpleIntegerProperty(tpID);
        this.tpStocks = new SimpleIntegerProperty(tpStocks);
        this.tpProductName = new SimpleStringProperty(tpProductName);
    }


}
