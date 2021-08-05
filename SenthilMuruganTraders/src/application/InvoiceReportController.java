package application;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

public class InvoiceReportController implements Initializable {

    @FXML
    private AnchorPane mainAnchorPane;

    @FXML
    private TextField searchTxt;
    @FXML
    private TextArea customerAddTxt;

    @FXML
    private Label invNoLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Label customerNameLabel;
    @FXML
    private Label customerNOLabel;
    @FXML
    private Label SubtotalLabel;
    @FXML
    private Label totalGSTLabel;
    @FXML
    private Label totalLabel;
    @FXML
    private Label amountGivenLabel;
    @FXML
    private Label balanceAmtLabel;

    //Buttons
    @FXML
    private Button searchBtn;
    @FXML
    private Button savePDFBtn;
    @FXML
    private Button printBtn;
    @FXML
    private Button deleteInvoiceBtn;
    @FXML
    private Button showInvoiceBtn;
    @FXML
    private Button showAllBtn;

    //TableView
    @FXML
    private TableView<InvoiceList> allReportsTableView;
    @FXML
    private TableColumn<InvoiceList , String> invoiceNumberCol;
    @FXML
    private TableColumn<InvoiceList , String> customerNameCol;
    @FXML
    private TableColumn<InvoiceList , Double> totalAmtCol;
    @FXML
    private TableColumn<InvoiceList , String> dateCol;

    //Invoice Table column
    @FXML
    TableView<Product> invoiceTableView;
    @FXML
    TableColumn<Product , Integer> productIDCol;
    @FXML
    TableColumn<Product , String> productNameCol;
    @FXML
    TableColumn<Product , Double> priceCol;
    @FXML
    TableColumn<Product , Integer> qtyCol;
    @FXML
    TableColumn<Product , Integer> gstPerCol;
    @FXML
    TableColumn<Product , Double> gstAmtCol;
    @FXML
    TableColumn<Product , Double> amountCol;

    DBConnection connectNow =  new DBConnection();
    Connection connectDB = connectNow.getConnection();
    Double totalItem = 0.0;
    String gst5l = "";
    String sgst5l = "";
    String cgst5l = "";

    String gst18l = "";
    String sgst18l = "";
    String cgst18l = "";

    String gst28l = "";
    String sgst28l = "";
    String cgst28l = "";
    @Override
    public void initialize(URL url,ResourceBundle resourceBundle)
    {
        searchTxt.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent k) {
                if (k.getCode().equals(KeyCode.ENTER)) {
                    search();
                }
            }
        });
        mainAnchorPane.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (showAllShortcut.match(event)) {
                    showAll();
                }
            }
        });
        mainAnchorPane.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (saveShortcut.match(event)) {
                    save();
                }
            }
        });
        mainAnchorPane.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (printShortcut.match(event)) {
                    print();
                }
            }
        });
        allReportsTableView.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (deleteShortcut.match(event)) {
                	deleteInvoice();
                }
            }
        });
        
        /*allReportsTableView.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent k) {
                if (k.getCode().equals(KeyCode.DELETE)) {
                    deleteInvoice();
                }
            }
        });*/
        allReportsTableView.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent k) {
                if (k.getCode().equals(KeyCode.ENTER)) {
                    show();
                }
            }
        });
        invoiceNumberCol.setCellValueFactory(new PropertyValueFactory<>("InvoiceNumber"));
        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("CustomerName"));
        totalAmtCol.setCellValueFactory(new PropertyValueFactory<>("TotalAmount"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("Date"));

        productIDCol.setCellValueFactory( new PropertyValueFactory<>("ProductID"));
        productNameCol.setCellValueFactory( new PropertyValueFactory<>("ProductName"));
        priceCol.setCellValueFactory( new PropertyValueFactory<>("ProductPrice"));
        qtyCol.setCellValueFactory( new PropertyValueFactory<>("Qty"));
        gstPerCol.setCellValueFactory(new PropertyValueFactory<>("ProductGSTPercentage"));
        gstAmtCol.setCellValueFactory(new PropertyValueFactory<>("ProductGST"));
        amountCol.setCellValueFactory( new PropertyValueFactory<>("Amount"));

        String getInvoices = "select InvNumber,CustomerName,TotalAmount,Date from InvoiceNumbers where ID != 1";
        String invoiceNumber = "";
        String customerName = "";
        Double totalAmt = 0.0;
        String date = "";
        try
        {
            Statement statement = connectDB.createStatement();
            ResultSet invoiceListSet = statement.executeQuery(getInvoices);
            while (invoiceListSet.next())
            {
                invoiceNumber = invoiceListSet.getString("InvNumber");
                customerName = invoiceListSet.getString("CustomerName");
                totalAmt = invoiceListSet.getDouble("TotalAmount");
                date = new SimpleDateFormat("dd/MM/yyyy").format(invoiceListSet.getDate("date"));

                InvoiceList invoiceList = new InvoiceList(invoiceNumber,customerName,totalAmt,date);
                allReportsTableView.getItems().add(invoiceList);

            }
        }catch (Exception e)
        {
            e.printStackTrace();
            e.getCause();
        }
    }
    public static final KeyCombination saveShortcut = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_ANY);
    public static final KeyCombination showAllShortcut = new KeyCodeCombination(KeyCode.A, KeyCombination.CONTROL_ANY);
    public static final KeyCombination printShortcut = new KeyCodeCombination(KeyCode.P, KeyCombination.CONTROL_ANY);
    public static final KeyCombination deleteShortcut = new KeyCodeCombination(KeyCode.D, KeyCombination.SHIFT_ANY);

    
    public void showAllBtnOnAction(ActionEvent event)
    {
        showAll();
    }
    public void showAll()
    {
        allReportsTableView.getItems().clear();
        String getInvoices = "select InvNumber,CustomerName,TotalAmount,Date from InvoiceNumbers where ID != 1";
        String invoiceNumber = "";
        String customerName = "";
        Double totalAmt = 0.0;
        String date = "";
        try
        {
            Statement statement = connectDB.createStatement();
            ResultSet invoiceListSet = statement.executeQuery(getInvoices);
            while (invoiceListSet.next())
            {
                invoiceNumber = invoiceListSet.getString("InvNumber");
                customerName = invoiceListSet.getString("CustomerName");
                totalAmt = invoiceListSet.getDouble("TotalAmount");
                date = new SimpleDateFormat("dd/MM/yyyy").format(invoiceListSet.getDate("date"));


                InvoiceList invoiceList = new InvoiceList(invoiceNumber,customerName,totalAmt,date);
                allReportsTableView.getItems().add(invoiceList);

            }
        }catch (Exception e)
        {
            e.printStackTrace();
            e.getCause();
        }

    }
    public void calcGstPercentages()
    {
        Integer curGstPer;
        Double curGstAmt;
        Double gst5 = 0.0;
        Double gst18 = 0.0;
        Double gst28 = 0.0;
        for (Product item : invoiceTableView.getItems())
        {
            curGstPer = gstPerCol.getCellObservableValue(item).getValue();
            curGstAmt = gstAmtCol.getCellObservableValue(item).getValue();
            if (curGstPer == 5)
            {
                gst5 = gst5 + curGstAmt;
            }
            else if (curGstPer == 18)
            {
                gst18 = gst18 + curGstAmt;
            }
            else
            {
                gst28 = gst28 + curGstAmt;
            }

        }

        if (gst5 != 0.0)
        {
            Double s5cgst = gst5/2;
            gst5l = "GST 5%"+" "+":"+" "+gst5+" "+" RS";
            sgst5l = "SGST 2.5% : "+String.valueOf(s5cgst)+" RS";
            cgst5l = "CGST 2.5% : "+String.valueOf(s5cgst)+" RS";
        }
        if (gst18 != 0.0)
        {
            Double s18cgst = gst18/2;
            gst18l = "GST 18%"+" "+":"+" "+gst18+" "+" RS";
            cgst18l = "CGST 9%    : "+String.valueOf(s18cgst)+" RS";
            sgst18l = "SGST 9%    : "+String.valueOf(s18cgst)+" RS";
        }
        if (gst28 != 0.0)
        {
            Double s28gst = gst28/2;
            gst28l = "GST 28%"+" "+":"+" "+gst28+" "+"RS";
            sgst28l = "SGST 14%  : "+String.valueOf(s28gst)+" RS";
            cgst28l = "CGST 14%  : "+String.valueOf(s28gst)+" RS";
        }
    }
    public void showBtnOnAction(ActionEvent event)
    {
        show();
    }
    public void show()
    {
        invoiceTableView.getItems().clear();
        ObservableList<InvoiceList> selectedInvoice;
        selectedInvoice = allReportsTableView.getSelectionModel().getSelectedItems();
        String selectedInvNum = null;
        String selectedDate = "";
        for (InvoiceList res : selectedInvoice)
        {
            selectedInvNum = res.getInvoiceNumber();
            selectedDate = res.getDate();

        }

        String getInvoiceReport = "select * from "+selectedInvNum+" ";
        try
        {
            Statement statement = connectDB.createStatement();
            ResultSet invoiceDetailSet = statement.executeQuery(getInvoiceReport);
            String invoice_ID = null;
            String customer_name = null;
            String customer_number = null;
            String customer_address = null;
            Double subTotalamt = null;
            Double total_gstAmount = null;
            Double total_amount = null;
            Double balance_amount = null;

            while (invoiceDetailSet.next())
            {
                invoice_ID = invoiceDetailSet.getString("InvoiceID");
                Integer product_ID = invoiceDetailSet.getInt("ProductID");
                String product_name = invoiceDetailSet.getString("Description");
                Double product_price = invoiceDetailSet.getDouble("Price");
                Double qty = invoiceDetailSet.getDouble("Quantity");
                Integer gst_Per = invoiceDetailSet.getInt("GSTP");
                Double gst_Amt = invoiceDetailSet.getDouble("GSTAmt");
                Double amount = invoiceDetailSet.getDouble("Amount");
                subTotalamt = invoiceDetailSet.getDouble("SubTotal");
                total_gstAmount = invoiceDetailSet.getDouble("TotalGSTAmount");
                total_amount = invoiceDetailSet.getDouble("TotalAmount");
                customer_name = invoiceDetailSet.getString("CustomerName");
                customer_number = invoiceDetailSet.getString("CustomerNumber");
                customer_address = invoiceDetailSet.getString("CustomerAddress");
                balance_amount = invoiceDetailSet.getDouble("Balance");

                Product product = new Product(product_name,product_ID,product_price,qty,gst_Per,gst_Amt,amount);

                invoiceTableView.getItems().add(product);
            }

            invNoLabel.setText(invoice_ID);
            dateLabel.setText(selectedDate);
            customerNameLabel.setText(customer_name);
            customerNOLabel.setText(customer_number);
            customerAddTxt.setText(customer_address);
            SubtotalLabel.setText(String.valueOf(subTotalamt));
            totalGSTLabel.setText(String.valueOf(total_gstAmount));
            totalLabel.setText(String.valueOf(total_amount));
            Double amountGiven = Double.valueOf(total_amount) + balance_amount;
            amountGivenLabel.setText(String.valueOf(amountGiven));
            balanceAmtLabel.setText(String.valueOf(balance_amount));

        }catch (Exception e)
        {
            e.printStackTrace();
            e.getCause();
        }
    }
    public void saveAsPDFbtnOnAction(ActionEvent event)
    {
        save();

    }
    public void save()
    {
        for (Product res: invoiceTableView.getItems())
        {
            Double curQty = res.getQty();
            totalItem = totalItem + curQty;
        }
        calcGstPercentages();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName(invNoLabel.getText()+customerNameLabel.getText()+".pdf");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf")

        );
        File selectedFile = fileChooser.showSaveDialog(null);
        ObservableList<Product> allProductsPrint;
        allProductsPrint = invoiceTableView.getItems();

        JRBeanCollectionDataSource itemJRBean= new JRBeanCollectionDataSource(allProductsPrint);
        Map<String,Object> parameters = new HashMap<String,Object>();
        parameters.put("CollectionBeanParam",itemJRBean);
        parameters.put("invoiceNumber",": "+invNoLabel.getText());
        parameters.put("totalAmount1",totalLabel.getText());
        parameters.put("dateField1","Date:"+dateLabel.getText());
        
        parameters.put("subTotal1",SubtotalLabel.getText());
        parameters.put("totalGSTAmount",totalGSTLabel.getText());
        parameters.put("totalAmount",totalLabel.getText());
        parameters.put("roundoffAmount",totalLabel.getText());
        parameters.put("amtGiven",amountGivenLabel.getText());
        parameters.put("balance",balanceAmtLabel.getText());
        parameters.put("custName",customerNameLabel.getText());
        /*parameters.put("CollectionBeanParameter",itemJRBean);
        parameters.put("invoiceNumber",": "+invNoLabel.getText());
        parameters.put("dateField","  "+dateLabel.getText());
        parameters.put("customerName",customerNameLabel.getText());
        parameters.put("customerAddress",customerAddTxt.getText());
        parameters.put("customerNumber",customerNOLabel.getText());
        parameters.put("subTotal",SubtotalLabel.getText());
        parameters.put("totalGSTAmount",totalGSTLabel.getText());
        parameters.put("totalAmount",totalLabel.getText());
        parameters.put("roundOffAmount",totalLabel.getText());
        parameters.put("sgst5", sgst5l);
        parameters.put("cgst5", cgst5l);
        parameters.put("sgst18",sgst18l);
        parameters.put("cgst18",cgst18l);
        parameters.put("sgst28",sgst28l);
        parameters.put("cgst28",cgst28l);
        parameters.put("noOfItems",String.valueOf(totalItem));
        parameters.put("amountGiven",amountGivenLabel.getText());
        parameters.put("balanceAmt",balanceAmtLabel.getText());*/
        try{
            InputStream input = new FileInputStream(new File("jasperReport/Blank_Letter.jrxml"));
            JasperDesign jasperDesign = JRXmlLoader.load(input);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,parameters,new JREmptyDataSource());
            String filename =selectedFile.getCanonicalPath();
            JasperExportManager.exportReportToPdfFile(jasperPrint,filename);

        }catch (IOException | JRException e){
            e.printStackTrace();
            e.getCause();
        }
    }
    public void printBtnOnAction(ActionEvent event)
    {
        print();
    }
    public void print()
    {
        totalItem = 0.0;
        for (Product res: invoiceTableView.getItems())
        {
            Double curQty = res.getQty();
            totalItem = totalItem + curQty;
        }
        calcGstPercentages();
        ObservableList<Product> allProductsPrint;
        allProductsPrint = invoiceTableView.getItems();

        JRBeanCollectionDataSource itemJRBean= new JRBeanCollectionDataSource(allProductsPrint);
        Map<String,Object> parameters = new HashMap<String,Object>();
        parameters.put("CollectionBeanParam",itemJRBean);
        parameters.put("invoiceNumber",": "+invNoLabel.getText());
        parameters.put("totalAmount1",totalLabel.getText());
        parameters.put("dateField1","Date:"+dateLabel.getText());
        
        parameters.put("subTotal1",SubtotalLabel.getText());
        parameters.put("totalGSTAmount",totalGSTLabel.getText());
        parameters.put("totalAmount",totalLabel.getText());
        parameters.put("roundoffAmount",totalLabel.getText());
        parameters.put("amtGiven",amountGivenLabel.getText());
        parameters.put("balance",balanceAmtLabel.getText());
        parameters.put("custName",customerNameLabel.getText());
        /*parameters.put("CollectionBeanParameter",itemJRBean);
        parameters.put("invoiceNumber",": "+invNoLabel.getText());
        parameters.put("dateField","  "+dateLabel.getText());
        parameters.put("customerName",customerNameLabel.getText());
        parameters.put("customerAddress",customerAddTxt.getText());
        parameters.put("customerNumber",customerNOLabel.getText());
        parameters.put("subTotal",SubtotalLabel.getText());
        parameters.put("totalGSTAmount",totalGSTLabel.getText());
        parameters.put("totalAmount",totalLabel.getText());
        parameters.put("roundOffAmount",totalLabel.getText());
        parameters.put("sgst5", sgst5l);
        parameters.put("cgst5", cgst5l);
        parameters.put("sgst18",sgst18l);
        parameters.put("cgst18",cgst18l);
        parameters.put("sgst28",sgst28l);
        parameters.put("cgst28",cgst28l);
        parameters.put("noOfItems",String.valueOf(totalItem));
        parameters.put("amountGiven",amountGivenLabel.getText());
        parameters.put("balanceAmt",balanceAmtLabel.getText());*/
        try{
            InputStream input = new FileInputStream(new File("jasperReport/Blank_Letter.jrxml"));
            JasperDesign jasperDesign = JRXmlLoader.load(input);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,parameters,new JREmptyDataSource());
            //JasperViewer.viewReport(jasperPrint);
            JasperPrintManager.printReport(jasperPrint,true);

        }catch (IOException | JRException e){
            e.printStackTrace();
            e.getCause();
        }
    }
    public void searchBtnOnAction(ActionEvent event)
    {
        search();
    }
    public void search()
    {
        allReportsTableView.getItems().clear();
        String searchVal = searchTxt.getText();
        //String getSearchData = "SELECT * FROM InvoiceNumbers WHERE ((InvNumber = '"+searchVal+"') or (CustomerName like '%"+searchVal+"%')) ";
        String getSearchData="select * from InvoiceNumbers where match (InvNumber,CustomerName) Against ('"+searchVal+"')";

        try
        {
            Statement statement = connectDB.createStatement();
            ResultSet searchSet = statement.executeQuery(getSearchData);
            if (!searchSet.isBeforeFirst())
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Data not matched");
                String s = "Incorrect Customer Name or Invoice Number";
                alert.setContentText(s);
                alert.showAndWait();
            }
            else
            {
                while (searchSet.next())
                {
                    String invoice_ID = searchSet.getString("InvNumber");
                    String customer_name = searchSet.getString("CustomerName");
                    Double totalAmount = searchSet.getDouble("TotalAmount");
                    String date = new SimpleDateFormat("dd/MM/yyyy").format(searchSet.getDate("Date"));
                    InvoiceList invoiceListSearch = new InvoiceList(invoice_ID,customer_name,totalAmount,date);
                    allReportsTableView.getItems().add(invoiceListSearch);

                }
            }

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }
    public void deleteInvoiceBtnOnAction(ActionEvent event)
    {
        deleteInvoice();
    }
    public void deleteInvoice()
    {
        ObservableList<InvoiceList> selectedInvoice;
        selectedInvoice = allReportsTableView.getSelectionModel().getSelectedItems();
        String selectedInvNum = null;
        String selectedDate = "";
        for (InvoiceList res : selectedInvoice)
        {
            selectedInvNum = res.getInvoiceNumber();
            selectedDate = res.getDate();
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete");
        String s = "Do you want to delete the Invoice Report permanently";
        alert.setContentText(s);
        Optional<ButtonType> result = alert.showAndWait();
        if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
            String deleteInvoice = "delete from InvoiceNumbers where InvNumber = '"+selectedInvNum+"' ";
            String deleteInvoiceTable = "drop table  "+selectedInvNum+" ";
            try
            {
                Statement statement = connectDB.createStatement();
                statement.executeUpdate(deleteInvoice);
                statement.executeUpdate(deleteInvoiceTable);
                showAll();
            }catch (Exception e)
            {
                e.printStackTrace();
                e.getCause();
            }
        }

    }
}
