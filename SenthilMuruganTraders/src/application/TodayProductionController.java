package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class TodayProductionController implements Initializable {
    @FXML
    private AreaChart areaChart;
    @FXML
    private Label totalAmountLabel;
    @FXML
    private Label customerLabel;
    @FXML
    private Label totalProductsLabel;
    //DatePicker
    @FXML
    private DatePicker productionDatePicker;
    @FXML
    private DatePicker salesDatePicker;
    //Button
    @FXML
    private Button salesMonthlyBtn;
    @FXML
    private Button salesYearlyBtn;
    @FXML
    private Button salesOnDateBtn;
    @FXML
    private Button monthlyProductionBtn;
    @FXML
    private Button yearlyProductionBtn;
    @FXML
    private Button onDateProductionBtn;
    @FXML
    private Button todayProductionBtn;
    @FXML
    private Button salesTodayBtn;
    @FXML
    private Button salesWeeklyBtn;
    @FXML
    private Button weeklyProductionBtn;

    //Tables
    @FXML
    private TableView<TodayProductionTable> todayProductionTableView;
    @FXML
    private TableColumn<TodayProductionTable , Integer > tpProductIDCol;
    @FXML
    private TableColumn<TodayProductionTable , String > tpProductNameCol;
    @FXML
    private TableColumn<TodayProductionTable , Integer > tpTotStocksCol;
    //TPInvoiceTable
    @FXML
    private TableView<TPInvoice> todaySalesTableView;
    @FXML
    private TableColumn<TPInvoice , String> invNumberCol;
    @FXML
    private TableColumn<TPInvoice , String> custNameCol;
    @FXML
    private TableColumn<TPInvoice , Double> totAmtCol;
    @FXML
    private TableColumn<TPInvoice , String> dateCol;
    @FXML
    private TableColumn<TPInvoice , Integer> totalSold;



    DBConnection connectNow = new DBConnection();
    Connection connectDB = connectNow.getConnection();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        invNumberCol.setCellValueFactory( new PropertyValueFactory<>("InvNumber"));
        custNameCol.setCellValueFactory( new PropertyValueFactory<>("CustomerName"));
        totAmtCol.setCellValueFactory( new PropertyValueFactory<>("TotalAmount"));
        dateCol.setCellValueFactory( new PropertyValueFactory<>("Date"));
        totalSold.setCellValueFactory( new PropertyValueFactory<>("ProductSold"));

        String getTodayProduction = "select distinct InvNumber,TotalAmount,CustomerName,ProductsSold,Date from InvoiceNumbers where cast(Date as Date) = cast(CURDATE() as Date)";

        //String getWeeklyData = "SELECT TotalAmount FROM InvoiceNumbers WHERE Date >= curdate() - INTERVAL DAYOFWEEK(curdate())+6 DAY AND Date < curdate() - INTERVAL DAYOFWEEK(curdate())-1 DAY;";
        String getWeeklyData = "select TotalAmount from InvoiceNumbers where extract(WEEK from Date) = WEEK(cast(CURDATE() as Date))";
        try
        {
            Statement statement = connectDB.createStatement();
            ResultSet todayProductionSet = statement.executeQuery(getTodayProduction);
            Double totalAmtSum = 0.0;
            Integer totalProductSum = 0;
            Integer count = 0;
            String inv_num = "";
            String cust_name = "";
            String date_val = "";
            while (todayProductionSet.next())
            {
                Double totalAmt = todayProductionSet.getDouble("TotalAmount");
                totalAmtSum = totalAmtSum+totalAmt;
                count=count+1;
                Integer totalProducts = todayProductionSet.getInt("ProductsSold");
                totalProductSum = totalProductSum + totalProducts;

                inv_num = todayProductionSet.getString("InvNumber");
                cust_name = todayProductionSet.getString("CustomerName");
                String date = new SimpleDateFormat("dd/MM/yyyy").format(todayProductionSet.getDate("Date"));
                TPInvoice tpInvoice = new TPInvoice(inv_num,cust_name,totalAmt,date,totalProducts);
                todaySalesTableView.getItems().add(tpInvoice);
            }
            totalAmountLabel.setText(String.valueOf(totalAmtSum));
            customerLabel.setText(String.valueOf(count));
            totalProductsLabel.setText(String.valueOf(totalProductSum));

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis(0,2000,50000);
        yAxis.setLabel("Rupees");

        areaChart.setTitle("Weekly Sales");
        XYChart.Series series = new XYChart.Series();
        series.setName("Weekly");
        try
        {
            Statement statement = connectDB.createStatement();
            ResultSet weeklyDataSet =  statement.executeQuery(getWeeklyData);
            Integer count = 1;
            while (weeklyDataSet.next())
            {
                series.getData().add(new XYChart.Data<>("Day"+count,weeklyDataSet.getDouble("TotalAmount")));
                count = count +1;

            }
            areaChart.getData().addAll(series);
        }catch (Exception e)
        {
            e.printStackTrace();
            e.getCause();
        }


        /*series.getData().add(new XYChart.Data<>("Monday",50000));
        series.getData().add(new XYChart.Data("Tuesday", 4000));
        series.getData().add(new XYChart.Data("Wednesday", 3000));
        series.getData().add(new XYChart.Data("Thursday", 5000));
        series.getData().add(new XYChart.Data("Friday", 4000));
        series.getData().add(new XYChart.Data("Saturday", 1000));
        series.getData().add(new XYChart.Data("Sunday", 1200));*/


        tpProductIDCol.setCellValueFactory(new PropertyValueFactory<>("TpID"));
        tpTotStocksCol.setCellValueFactory(new PropertyValueFactory<>("TpStocks"));
        tpProductNameCol.setCellValueFactory(new PropertyValueFactory<>("TpProductName"));

        String getTodayData = "select distinct TP_ID from TodayProduction where cast(Date as Date) = cast(CURDATE() as Date);";
        try
        {
            Statement statement = connectDB.createStatement();
            ResultSet todaySet = statement.executeQuery(getTodayData);
            Integer totalStock = 0;
            List<Integer> curIDList = new ArrayList<>();
            while (todaySet.next())
            {
                curIDList.add(todaySet.getInt("TP_ID"));
            }
            String curProductName = "";
            for (Integer curID : curIDList)
            {

                String getTotalStock = "select TodayStocks,ProductName from TodayProduction where TP_ID = '"+curID+"' and cast(Date as Date) = cast(CURDATE() as Date)" ;
                ResultSet totalStockSet = statement.executeQuery(getTotalStock);
                while (totalStockSet.next())
                {
                    totalStock = totalStock + totalStockSet.getInt("TodayStocks");
                    curProductName = totalStockSet.getString("ProductName");
                }
                TodayProductionTable todayProductionTable = new TodayProductionTable(curID,totalStock,curProductName);
                todayProductionTableView.getItems().add(todayProductionTable);
                totalStock = 0;
            }
        }catch (Exception e)
        {
            e.printStackTrace();
            e.getCause();
        }




    }
    public void onDateProductionBtnOnAction(ActionEvent event)
    {
        todayProductionTableView.getItems().clear();
        if (productionDatePicker.getValue() != null) {
            LocalDate localDate = productionDatePicker.getValue();
            String getOnDateData = "select distinct TP_ID from TodayProduction where cast(Date as Date) = cast('" + localDate + "' as Date);";
            try {
                Statement statement = connectDB.createStatement();
                ResultSet onDateSet = statement.executeQuery(getOnDateData);
                Integer totalStock = 0;
                List<Integer> curIDList = new ArrayList<>();
                while (onDateSet.next()) {
                    curIDList.add(onDateSet.getInt("TP_ID"));
                }
                String curProductName = "";
                for (Integer curID : curIDList) {

                    String getTotalStock = "select TodayStocks,ProductName from TodayProduction where TP_ID = '" + curID + "' and cast(Date as Date) = cast('" + localDate + "' as Date)";
                    ResultSet totalStockSet = statement.executeQuery(getTotalStock);
                    while (totalStockSet.next()) {
                        totalStock = totalStock + totalStockSet.getInt("TodayStocks");
                        curProductName = totalStockSet.getString("ProductName");
                    }
                    TodayProductionTable todayProductionTable = new TodayProductionTable(curID, totalStock, curProductName);
                    todayProductionTableView.getItems().add(todayProductionTable);
                    totalStock = 0;
                }
            } catch (Exception e) {
                e.printStackTrace();
                e.getCause();
            }
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            String s = "Select Date !";
            alert.setContentText(s);
            alert.showAndWait();
        }

    }
    public void weeklyProductionBtnOnAction(ActionEvent event)
    {
        todayProductionTableView.getItems().clear();

        if (productionDatePicker.getValue() != null) {
            LocalDate localDate = productionDatePicker.getValue();


            String getWeeklyData = "select distinct TP_ID from TodayProduction where extract(WEEK from Date) = WEEK(cast('" + localDate + "' as Date))";
            try {
                Statement statement = connectDB.createStatement();
                ResultSet weeklySet = statement.executeQuery(getWeeklyData);
                Integer totalStock = 0;
                List<Integer> curIDList = new ArrayList<>();
                while (weeklySet.next()) {
                    curIDList.add(weeklySet.getInt("TP_ID"));
                }
                String curProductName = "";
                for (Integer curID : curIDList) {

                    String getTotalStock = "select TodayStocks,ProductName from TodayProduction where TP_ID = '" + curID + "' and  extract(WEEK from Date) = WEEK(cast('" + localDate + "' as Date))";
                    ResultSet totalStockSet = statement.executeQuery(getTotalStock);
                    while (totalStockSet.next()) {
                        totalStock = totalStock + totalStockSet.getInt("TodayStocks");
                        curProductName = totalStockSet.getString("ProductName");
                    }
                    TodayProductionTable todayProductionTable = new TodayProductionTable(curID, totalStock, curProductName);
                    todayProductionTableView.getItems().add(todayProductionTable);
                    totalStock = 0;
                }
            } catch (Exception e) {
                e.printStackTrace();
                e.getCause();
            }
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            String s = "Select Date !";
            alert.setContentText(s);
            alert.showAndWait();
        }
    }
    public void monthlyProductionBtnOnAction(ActionEvent event)  {
        todayProductionTableView.getItems().clear();

        if (productionDatePicker.getValue() != null) {
            LocalDate date = productionDatePicker.getValue();
            int month = date.getMonthValue();
            String getMonthlyData = "SELECT DISTINCT TP_ID FROM TodayProduction WHERE EXTRACT(MONTH FROM Date) = '" + month + "' ";
            try {
                Statement statement = connectDB.createStatement();
                ResultSet monthlySet = statement.executeQuery(getMonthlyData);
                Integer totalStock = 0;
                List<Integer> curIDList = new ArrayList<>();
                while (monthlySet.next()) {
                    curIDList.add(monthlySet.getInt("TP_ID"));
                }
                String curProductName = "";
                for (Integer curID : curIDList) {

                    String getTotalStock = "select TodayStocks,ProductName from TodayProduction where TP_ID = '" + curID + "' and extract(MONTH from Date) = '" + month + "' ";
                    ResultSet totalStockSet = statement.executeQuery(getTotalStock);
                    while (totalStockSet.next()) {
                        totalStock = totalStock + totalStockSet.getInt("TodayStocks");
                        curProductName = totalStockSet.getString("ProductName");
                    }
                    TodayProductionTable todayProductionTable = new TodayProductionTable(curID, totalStock, curProductName);
                    todayProductionTableView.getItems().add(todayProductionTable);
                    totalStock = 0;
                }
            } catch (Exception e) {
                e.printStackTrace();
                e.getCause();
            }
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            String s = "Select Date !";
            alert.setContentText(s);
            alert.showAndWait();
        }

    }
    public void yearlyProductionBtnOnAction(ActionEvent event)
    {
        todayProductionTableView.getItems().clear();

        if (productionDatePicker.getValue() != null) {
            LocalDate date = productionDatePicker.getValue();
            int year = date.getYear();
            String getYearlyData = "SELECT DISTINCT TP_ID FROM TodayProduction WHERE EXTRACT(YEAR FROM Date) = '" + year + "' ";
            try {
                Statement statement = connectDB.createStatement();
                ResultSet yearlySet = statement.executeQuery(getYearlyData);
                Integer totalStock = 0;
                List<Integer> curIDList = new ArrayList<>();
                while (yearlySet.next()) {
                    curIDList.add(yearlySet.getInt("TP_ID"));
                }
                String curProductName = "";
                for (Integer curID : curIDList) {

                    String getTotalStock = "select TodayStocks,ProductName from TodayProduction where TP_ID = '" + curID + "' and extract(YEAR from Date) = '" + year + "'";
                    ResultSet totalStockSet = statement.executeQuery(getTotalStock);
                    while (totalStockSet.next()) {
                        totalStock = totalStock + totalStockSet.getInt("TodayStocks");
                        curProductName = totalStockSet.getString("ProductName");
                    }
                    TodayProductionTable todayProductionTable = new TodayProductionTable(curID, totalStock, curProductName);
                    todayProductionTableView.getItems().add(todayProductionTable);
                    totalStock = 0;
                }
            } catch (Exception e) {
                e.printStackTrace();
                e.getCause();
            }
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            String s = "Select Date !";
            alert.setContentText(s);
            alert.showAndWait();
        }
    }
    public void todayProductionBtnOnAction (ActionEvent event)
    {
        todayProductionTableView.getItems().clear();
        String getTodayData = "select distinct TP_ID from TodayProduction where cast(Date as Date) = cast(CURDATE() as Date);";
        try
        {
            Statement statement = connectDB.createStatement();
            ResultSet todaySet = statement.executeQuery(getTodayData);
            Integer totalStock = 0;
            List<Integer> curIDList = new ArrayList<>();
            while (todaySet.next())
            {
                curIDList.add(todaySet.getInt("TP_ID"));
            }
            String curProductName = "";
            for (Integer curID : curIDList)
            {

                String getTotalStock = "select TodayStocks,ProductName from TodayProduction where TP_ID = '"+curID+"' and cast(Date as Date) = cast(CURDATE() as Date)";
                ResultSet totalStockSet = statement.executeQuery(getTotalStock);
                while (totalStockSet.next())
                {
                    totalStock = totalStock + totalStockSet.getInt("TodayStocks");
                    curProductName = totalStockSet.getString("ProductName");
                }
                TodayProductionTable todayProductionTable = new TodayProductionTable(curID,totalStock,curProductName);
                todayProductionTableView.getItems().add(todayProductionTable);
                totalStock = 0;
            }
        }catch (Exception e)
        {
            e.printStackTrace();
            e.getCause();
        }
    }
    //Invoice Table Methods
    public void salesWeeklyBtnOnAction(ActionEvent event)
    {
        if (salesDatePicker.getValue() != null)
        {
            todaySalesTableView.getItems().clear();
            LocalDate localDate = salesDatePicker.getValue();
            CategoryAxis xAxis = new CategoryAxis();
            NumberAxis yAxis = new NumberAxis(0, 2000, 50000);
            yAxis.setLabel("Rupees");
            String getWeeklySales = "select InvNumber,TotalAmount,CustomerName,ProductsSold,Date from InvoiceNumbers where extract(WEEK from Date) = WEEK(cast('" + localDate + "' as Date))";
            try {
                Statement statement = connectDB.createStatement();
                ResultSet weeklySalesSet = statement.executeQuery(getWeeklySales);
                Double totalAmtSum = 0.0;
                Integer totalProductSum = 0;
                Integer count = 0;
                String inv_num = "";
                String cust_name = "";
                String date_val = "";
                areaChart.setTitle("Monthly Sales");
                XYChart.Series series = new XYChart.Series();
                series.setName("Monthly");
                Integer countDay = 1;
                while (weeklySalesSet.next()) {
                    Double totalAmt = weeklySalesSet.getDouble("TotalAmount");
                    totalAmtSum = totalAmtSum + totalAmt;
                    count = count + 1;
                    Integer totalProducts = weeklySalesSet.getInt("ProductsSold");
                    totalProductSum = totalProductSum + totalProducts;

                    inv_num = weeklySalesSet.getString("InvNumber");
                    cust_name = weeklySalesSet.getString("CustomerName");
                    String date = new SimpleDateFormat("dd/MM/yyyy").format(weeklySalesSet.getDate("Date"));
                    TPInvoice tpInvoice = new TPInvoice(inv_num, cust_name, totalAmt, date, totalProducts);
                    todaySalesTableView.getItems().add(tpInvoice);
                    series.getData().add(new XYChart.Data<>("Day" + countDay, weeklySalesSet.getDouble("TotalAmount")));
                    countDay = countDay + 1;
                }
                totalAmountLabel.setText(String.valueOf(totalAmtSum));
                customerLabel.setText(String.valueOf(count));
                totalProductsLabel.setText(String.valueOf(totalProductSum));
                areaChart.getData().clear();
                areaChart.getData().addAll(series);

            } catch (Exception e) {
                e.printStackTrace();
                e.getCause();
            }
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            String s = "Select Date !";
            alert.setContentText(s);
            alert.showAndWait();
        }

    }
    public void salesMonthlyBtnOnAction(ActionEvent event)
    {
        if (salesDatePicker.getValue() != null) {
            todaySalesTableView.getItems().clear();
            LocalDate localDate = salesDatePicker.getValue();
            Integer month = localDate.getMonthValue();
            CategoryAxis xAxis = new CategoryAxis();
            NumberAxis yAxis = new NumberAxis(0, 2000, 50000);
            yAxis.setLabel("Rupees");
            String getMonthlySales = "select InvNumber,TotalAmount,CustomerName,ProductsSold,Date from InvoiceNumbers where extract(MONTH from Date) = '" + month + "'";
            try {
                Statement statement = connectDB.createStatement();
                ResultSet monthlySalesSet = statement.executeQuery(getMonthlySales);
                Double totalAmtSum = 0.0;
                Integer totalProductSum = 0;
                Integer count = 0;
                String inv_num = "";
                String cust_name = "";
                String date_val = "";
                areaChart.setTitle("Monthly Sales");
                XYChart.Series series = new XYChart.Series();
                series.setName("Monthly");
                Integer countDay = 1;
                while (monthlySalesSet.next()) {
                    Double totalAmt = monthlySalesSet.getDouble("TotalAmount");
                    totalAmtSum = totalAmtSum + totalAmt;
                    count = count + 1;
                    Integer totalProducts = monthlySalesSet.getInt("ProductsSold");
                    totalProductSum = totalProductSum + totalProducts;

                    inv_num = monthlySalesSet.getString("InvNumber");
                    cust_name = monthlySalesSet.getString("CustomerName");
                    String date = new SimpleDateFormat("dd/MM/yyyy").format(monthlySalesSet.getDate("Date"));
                    TPInvoice tpInvoice = new TPInvoice(inv_num, cust_name, totalAmt, date, totalProducts);
                    todaySalesTableView.getItems().add(tpInvoice);
                    series.getData().add(new XYChart.Data<>("Day" + countDay, monthlySalesSet.getDouble("TotalAmount")));
                    countDay = countDay + 1;
                }
                totalAmountLabel.setText(String.valueOf(totalAmtSum));
                customerLabel.setText(String.valueOf(count));
                totalProductsLabel.setText(String.valueOf(totalProductSum));
                areaChart.getData().clear();
                areaChart.getData().addAll(series);

            } catch (Exception e) {
                e.printStackTrace();
                e.getCause();
            }
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            String s = "Select Date !";
            alert.setContentText(s);
            alert.showAndWait();
        }

    }
    public void salesYearlyBtnOnAction(ActionEvent event)
    {
        if (salesDatePicker.getValue() != null) {
            todaySalesTableView.getItems().clear();
            LocalDate localDate = salesDatePicker.getValue();
            Integer year = localDate.getYear();
            String getYearlySales = "select InvNumber,TotalAmount,CustomerName,ProductsSold,Date from InvoiceNumbers where extract(YEAR from Date) = '" + year + "'";
            areaChart.setTitle("Yearly Sales");
            XYChart.Series series = new XYChart.Series();
            series.setName("Yearly");
            Integer countDay = 1;

            try {
                Statement statement = connectDB.createStatement();
                ResultSet yearlySalesSet = statement.executeQuery(getYearlySales);
                Double totalAmtSum = 0.0;
                Integer totalProductSum = 0;
                Integer count = 0;
                String inv_num = "";
                String cust_name = "";
                String date_val = "";
                while (yearlySalesSet.next()) {
                    Double totalAmt = yearlySalesSet.getDouble("TotalAmount");
                    totalAmtSum = totalAmtSum + totalAmt;
                    count = count + 1;
                    Integer totalProducts = yearlySalesSet.getInt("ProductsSold");
                    totalProductSum = totalProductSum + totalProducts;

                    inv_num = yearlySalesSet.getString("InvNumber");
                    cust_name = yearlySalesSet.getString("CustomerName");
                    String date = new SimpleDateFormat("dd/MM/yyyy").format(yearlySalesSet.getDate("Date"));
                    TPInvoice tpInvoice = new TPInvoice(inv_num, cust_name, totalAmt, date, totalProducts);
                    todaySalesTableView.getItems().add(tpInvoice);
                    series.getData().add(new XYChart.Data<>("Day" + countDay, yearlySalesSet.getDouble("TotalAmount")));
                    countDay = countDay + 1;
                }
                totalAmountLabel.setText(String.valueOf(totalAmtSum));
                customerLabel.setText(String.valueOf(count));
                totalProductsLabel.setText(String.valueOf(totalProductSum));
                areaChart.getData().clear();
                areaChart.getData().addAll(series);

            } catch (Exception e) {
                e.printStackTrace();
                e.getCause();
            }
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            String s = "Select Date !";
            alert.setContentText(s);
            alert.showAndWait();
        }
    }
    public void salesOnDateBtnOnAction(ActionEvent event)
    {
        if (salesDatePicker.getValue() != null) {
            todaySalesTableView.getItems().clear();
            LocalDate localDate = salesDatePicker.getValue();
            String getOnDateProduction = "select InvNumber,TotalAmount,CustomerName,ProductsSold,Date from InvoiceNumbers where cast(Date as Date) = cast('" + localDate + "' as Date)";
            areaChart.setTitle("Sales");
            XYChart.Series series = new XYChart.Series();
            series.setName("On Date");
            try {
                Statement statement = connectDB.createStatement();
                ResultSet onDateProductionSet = statement.executeQuery(getOnDateProduction);
                Double totalAmtSum = 0.0;
                Integer totalProductSum = 0;
                Integer count = 0;
                String inv_num = "";
                String cust_name = "";
                String date_val = "";
                while (onDateProductionSet.next()) {
                    Double totalAmt = onDateProductionSet.getDouble("TotalAmount");
                    totalAmtSum = totalAmtSum + totalAmt;
                    count = count + 1;
                    Integer totalProducts = onDateProductionSet.getInt("ProductsSold");
                    totalProductSum = totalProductSum + totalProducts;

                    inv_num = onDateProductionSet.getString("InvNumber");
                    cust_name = onDateProductionSet.getString("CustomerName");
                    String date = new SimpleDateFormat("dd/MM/yyyy").format(onDateProductionSet.getDate("Date"));
                    TPInvoice tpInvoice = new TPInvoice(inv_num, cust_name, totalAmt, date, totalProducts);
                    todaySalesTableView.getItems().add(tpInvoice);
                    series.getData().add(new XYChart.Data<>(inv_num, onDateProductionSet.getDouble("TotalAmount")));
                }
                totalAmountLabel.setText(String.valueOf(totalAmtSum));
                customerLabel.setText(String.valueOf(count));
                totalProductsLabel.setText(String.valueOf(totalProductSum));
                areaChart.getData().clear();
                areaChart.getData().addAll(series);

            } catch (Exception e) {
                e.printStackTrace();
                e.getCause();
            }
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            String s = "Select Date !";
            alert.setContentText(s);
            alert.showAndWait();
        }
    }

    public void salesTodayBtnOnAction(ActionEvent event)
    {
        areaChart.getData().clear();
        String getTodayProduction = "select InvNumber,TotalAmount,CustomerName,ProductsSold,Date from InvoiceNumbers where cast(Date as Date) = cast(CURDATE() as Date);";
        areaChart.setTitle("Today Sales");
        XYChart.Series series = new XYChart.Series();
        series.setName("Today");
        try
        {
            Statement statement = connectDB.createStatement();
            ResultSet todayProductionSet = statement.executeQuery(getTodayProduction);
            Double totalAmtSum = 0.0;
            Integer totalProductSum = 0;
            Integer count = 0;
            String inv_num = "";
            String cust_name = "";
            String date_val = "";
            while (todayProductionSet.next())
            {
                Double totalAmt = todayProductionSet.getDouble("TotalAmount");
                totalAmtSum = totalAmtSum+totalAmt;
                count=count+1;
                Integer totalProducts = todayProductionSet.getInt("ProductsSold");
                totalProductSum = totalProductSum + totalProducts;

                inv_num = todayProductionSet.getString("InvNumber");
                cust_name = todayProductionSet.getString("CustomerName");
                String date = new SimpleDateFormat("dd/MM/yyyy").format(todayProductionSet.getDate("Date"));
                TPInvoice tpInvoice = new TPInvoice(inv_num,cust_name,totalAmt,date,totalProducts);
                todaySalesTableView.getItems().add(tpInvoice);
                series.getData().add(new XYChart.Data<>(inv_num, todayProductionSet.getDouble("TotalAmount")));
            }
            totalAmountLabel.setText(String.valueOf(totalAmtSum));
            customerLabel.setText(String.valueOf(count));
            totalProductsLabel.setText(String.valueOf(totalProductSum));
            areaChart.getData().clear();
            areaChart.getData().addAll(series);

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }
}

