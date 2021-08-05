package application;


import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.*;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.*;
import java.net.URL;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.text.SimpleDateFormat;
import java.time.LocalDate;

import java.util.*;




import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import org.controlsfx.control.textfield.TextFields;



import static java.lang.Math.round;

public class SalesController implements Initializable {

    @FXML
    private ImageView brandImageView;
    @FXML
    private TableView<Product> tableView;
    @FXML
    private TableColumn<Product,Integer> productIDCol;
    @FXML
    private TableColumn<Product , String> productNameCol;
    @FXML
    private TableColumn<Product,Double> productPriceCol;
    @FXML
    private TableColumn<Product,Double> qtyCol;
    @FXML
    private TableColumn<Product,Integer> gstPercentageCol;
    @FXML
    private TableColumn<Product,Double> gstAmountCol;
    @FXML
    private TableColumn<Product,Double> amountCol;


    @FXML
    private Button addBtn;
    @FXML
    private TextField productNameTxt;
    @FXML
    private TextField qtyTxt;
    @FXML
    private Label subTotalLbl;
    @FXML
    private Label gstLbl;
    @FXML
    private Label gstAmountLbl;
    @FXML
    private Label totalGstAmtLbl;
    @FXML
    private Label roundOffLbl;
    @FXML
    private Label totalAmountLbl;
    @FXML
    private Label invoiceNoLbl;
    @FXML
    private Label dateLabel;
    @FXML
    private Button printBtn;

    //Customer Details
    @FXML
    private TextField customerNameTxt;
    @FXML
    private TextField customerNumberTxt;
    @FXML
    private TextArea customerAddressTxt;

    //GSTLabels
    @FXML
    private Label gst5Label;
    @FXML
    private Label sgst5Label;
    @FXML
    private Label igst5Label;
    @FXML
    private Label cgst5Label;

    @FXML
    private Label gst18Label;
    @FXML
    private Label sgst18Label;
    @FXML
    private Label igst18Label;
    @FXML
    private Label cgst18Label;

    @FXML
    private Label gst28Label;
    @FXML
    private Label sgst28Label;
    @FXML
    private Label igst28Label;
    @FXML
    private Label cgst28Label;

    @FXML
    private Label totalItemsLabel;
    @FXML
    private TextField balanceCalcTxt;
    @FXML
    private Label balanceAmtLabel;

    @FXML
    private AnchorPane mainAnchor;



    private Pane view;

    DBConnection connectNow = new DBConnection();
    Connection connectDB = connectNow.getConnection();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Date curdate = new Date();
        String date = new SimpleDateFormat("dd/MM/yyyy").format(curdate);
        dateLabel.setText(date);
        mainAnchor.setFocusTraversable(false);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                productNameTxt.requestFocus();
            }
        });


        qtyTxt.setOnKeyPressed(new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent k) {
            if (k.getCode().equals(KeyCode.ENTER)) {
                add();
                }
            }
        });
        tableView.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent k) {
                if (k.getCode().equals(KeyCode.DELETE)) {
                    remove();
                }
            }
        });


        mainAnchor.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (resetShortcut.match(event)) {
                    reset();
                }
            }
        });
        mainAnchor.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (submitShortcut.match(event))
                {
                    submit();
                }
            }
        });
        mainAnchor.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (saveShortcut.match(event))
                {
                    save();
                }
            }
        });
        
       
        productNameTxt.setOnKeyPressed((KeyEvent event) -> {switch(event.getCode()) {
        case ENTER:qtyTxt.requestFocus();}});
        


        try {
            String getAllProducts = "select ProductName from productDetail";
            Statement statement = connectDB.createStatement();
            ResultSet allProducts = statement.executeQuery(getAllProducts);
            ArrayList<String> allProductList = new ArrayList<>();
            while (allProducts.next())
            {
                allProductList.add(allProducts.getString("ProductName"));
            }

            TextFields.bindAutoCompletion(productNameTxt,allProductList);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throwables.getCause();
        }


        productIDCol.setCellValueFactory( new PropertyValueFactory<>("ProductID"));
        productNameCol.setCellValueFactory( new PropertyValueFactory<>("ProductName"));
        productPriceCol.setCellValueFactory( new PropertyValueFactory<>("ProductPrice"));
        qtyCol.setCellValueFactory( new PropertyValueFactory<>("Qty"));
        gstPercentageCol.setCellValueFactory(new PropertyValueFactory<>("ProductGSTPercentage"));
        gstAmountCol.setCellValueFactory(new PropertyValueFactory<>("ProductGST"));
        amountCol.setCellValueFactory( new PropertyValueFactory<>("Amount"));
        //tableView.setItems(observableList);
        String getinvNum = "select ID from InvoiceNumbers order by ID DESC LIMIT 1";
        try
        {
            Statement statement = connectDB.createStatement();
            ResultSet invNumSet = statement.executeQuery(getinvNum);
            Integer invValue = 0;
            while (invNumSet.next())
            {
                invValue = invNumSet.getInt("ID");
            }
            LocalDate currentDate = LocalDate.now();
            invValue=invValue+1;
            String tableName = "INV"+invValue+currentDate.getMonthValue()+currentDate.getYear();
            invoiceNoLbl.setText(tableName);

        }catch (Exception e)
        {

        }

    }

    /*ObservableList<Product> observableList = FXCollections.observableArrayList(
            new Product("M-Sand",1,3500.00,2,3500.00*2)
    );*/

    public void addOnAction(ActionEvent event)
    {
        add();
    }
    public static final KeyCombination resetShortcut = new KeyCodeCombination(KeyCode.R, KeyCombination.CONTROL_ANY);
    public static final KeyCombination submitShortcut = new KeyCodeCombination(KeyCode.EQUALS, KeyCombination.CONTROL_ANY);
    public static final KeyCombination saveShortcut = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_ANY);



    public void add()
    {
        String productName = productNameTxt.getText();
        String getProductDetail = "select ID,ProductName,productPrice,GST,date from productDetail where ProductName = '"+productName+"'";

        try {
            Statement statement = connectDB.createStatement();
            ResultSet productQuerySet = statement.executeQuery(getProductDetail);


            while (productQuerySet.next())
            {

                String s = new SimpleDateFormat("MM/dd/yyyy").format(productQuerySet.getDate("date"));



                Integer product_ID = productQuerySet.getInt("ID");
                String product_name = productQuerySet.getString("ProductName");
                Double product_price = productQuerySet.getDouble("productPrice");

                Integer gst = productQuerySet.getInt("GST");
                Double gstamt = 0.0;
                Double amt= 0.0;
                Double quantity = 1.0;


                if (qtyTxt.getText().isBlank()==false)
                {
                    quantity = Double.valueOf(qtyTxt.getText());
                    gstamt = ((product_price*quantity)*gst)/100;
                    amt = (product_price*quantity);
                    Product product = new Product(product_name,product_ID,product_price,quantity,gst,gstamt,amt);
                    tableView.getItems().add(product);
                }
                else
                {
                    gstamt = ((product_price)*gst)/100;
                    amt = product_price+gstamt;
                    Product product = new Product(product_name,product_ID,product_price,quantity,gst,gstamt,amt);
                    tableView.getItems().add(product);
                }

            }
            Double sum =0.0;
            List<Double> arrList = new ArrayList<>();
            for (Product item : tableView.getItems())
            {
                arrList.add(amountCol.getCellObservableValue(item).getValue());

            }
            for(Double d: arrList)
                sum += d;
            subTotalLbl.setText(+sum+"");
            productNameTxt.setText("");
            qtyTxt.setText("");
            productNameTxt.requestFocus();

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    public void submitOnAction(ActionEvent event)
    {
        submit();
    }
    public void submit()
    {
        Double gstSum = 0.0;
        Integer totalQty = 0;
        List<Double> gstList = new ArrayList<>();
        Integer count = 0;
        for (Product item : tableView.getItems())
        {
            gstList.add(gstAmountCol.getCellObservableValue(item).getValue());
            Double curtotalQty = qtyCol.getCellObservableValue(item).getValue();
            totalQty = totalQty + curtotalQty.intValue();
            count=count+1;
        }
        totalItemsLabel.setText(String.valueOf(count));
        for(Double d:gstList)
            gstSum+=d;
        totalGstAmtLbl.setText(+gstSum+"");
        Double sum = Double.valueOf(subTotalLbl.getText());
        Double totalAmount = sum+gstSum;
        Long roundTotalAmount = round(totalAmount);
        if (roundTotalAmount<totalAmount){
            roundTotalAmount=roundTotalAmount+1;
        }
        calcGstPercentages();
        totalAmountLbl.setText(+totalAmount+"");
        roundOffLbl.setText(+roundTotalAmount+"");
        productNameTxt.requestFocus();
        String amountGiven = "";
        Long balanceAmt = Long.valueOf(0);
        if (balanceCalcTxt.getText().isBlank()==false)
        {
            amountGiven = balanceCalcTxt.getText();
            balanceAmt = Long.valueOf(amountGiven) - Long.valueOf(roundOffLbl.getText());
            balanceAmtLabel.setText(String.valueOf(balanceAmt));
        }
        else
        {
            balanceAmtLabel.setText(String.valueOf(balanceAmt));
        }
    }
    public void calcGstPercentages()
    {
        Integer curGstPer;
        Double curGstAmt;
        Double gst5 = 0.0;
        Double gst18 = 0.0;
        Double gst28 = 0.0;
        for (Product item : tableView.getItems())
        {
            curGstPer = gstPercentageCol.getCellObservableValue(item).getValue();
            curGstAmt = gstAmountCol.getCellObservableValue(item).getValue();
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
            gst5Label.setText("GST 5%"+" "+":"+" "+gst5+" "+"RS");
            sgst5Label.setText("SGST 2.5% : "+String.valueOf(s5cgst)+"RS");
            cgst5Label.setText("CGST 2.5% : "+String.valueOf(s5cgst)+"RS");
        }
        if (gst18 != 0.0)
        {
            Double s18cgst = gst18/2;
            gst18Label.setText("GST 18%"+" "+":"+" "+gst18+" "+"RS");
            cgst18Label.setText("CGST 9% : "+String.valueOf(s18cgst)+"RS");
            sgst18Label.setText("SGST 9% : "+String.valueOf(s18cgst)+"RS");
        }
        if (gst28 != 0.0)
        {
            Double s28gst = gst28/2;
            gst28Label.setText("GST 28%"+" "+":"+" "+gst28+" "+"RS");
            sgst28Label.setText("SGST 14% : "+String.valueOf(s28gst)+"RS");
            cgst28Label.setText("CGST 14% : "+String.valueOf(s28gst)+"RS");
        }
    }

    public void removeBtnOnAction(ActionEvent event)
    {
        remove();


    }
    public void remove()
    {
        ObservableList<Product> allProducts,singleProduct;
        allProducts = tableView.getItems();
        singleProduct = tableView.getSelectionModel().getSelectedItems();
        if (tableView.getItems().size()!=1)
        {
            singleProduct.forEach(allProducts::remove);
            Double sum =0.0;
            List<Double> arrList = new ArrayList<>();
            for (Product item : tableView.getItems())
            {
                arrList.add(amountCol.getCellObservableValue(item).getValue());

            }
            for(Double d: arrList)
                sum += d;
            subTotalLbl.setText(+sum+"");
            productNameTxt.requestFocus();

        }
        else
        {
            allProducts.clear();
            subTotalLbl.setText("0.0");
            productNameTxt.requestFocus();
        }
    }
    public void jasperReport()
    {

    }
    public void print()
    {
        /*String outputFile = "/home/veera/Documents/new"+"report.pdf";
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName(invoiceNoLbl.getText()+".pdf");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf")

        );
        File selectedFile = fileChooser.showSaveDialog(null);*/



    	String outputFile = "/home/veera/Documents/new"+"report.pdf";
        ObservableList<Product> allProductsPrint;
        allProductsPrint = tableView.getItems();

        JRBeanCollectionDataSource itemJRBean= new JRBeanCollectionDataSource(allProductsPrint);
        Map<String,Object> parameters = new HashMap<String,Object>();
        parameters.put("CollectionBeanParam",itemJRBean);
        parameters.put("invoiceNumber",": "+invoiceNoLbl.getText());
        parameters.put("totalAmount1",totalAmountLbl.getText());
        parameters.put("dateField1","Date:"+dateLabel.getText());
        
        parameters.put("subTotal1",subTotalLbl.getText());
        parameters.put("totalGSTAmount",totalGstAmtLbl.getText());
        parameters.put("totalAmount",totalAmountLbl.getText());
        parameters.put("roundoffAmount",roundOffLbl.getText());
        parameters.put("amtGiven",balanceCalcTxt.getText());
        parameters.put("balance",balanceAmtLabel.getText());
        parameters.put("custName",customerNameTxt.getText());
        /*parameters.put("dateField","  "+dateLabel.getText());
        parameters.put("customerName",customerNameTxt.getText());
        parameters.put("customerAddress",customerAddressTxt.getText());
        parameters.put("customerNumber",customerNumberTxt.getText());
        parameters.put("subTotal",subTotalLbl.getText());
        parameters.put("totalGSTAmount",totalGstAmtLbl.getText());
        parameters.put("totalAmount",totalAmountLbl.getText());
        parameters.put("roundOffAmount",roundOffLbl.getText());
        parameters.put("sgst5", sgst5Label.getText());
        parameters.put("cgst5", cgst5Label.getText());
        parameters.put("sgst18",sgst18Label.getText());
        parameters.put("cgst18",cgst18Label.getText());
        parameters.put("sgst28",sgst28Label.getText());
        parameters.put("cgst28",cgst28Label.getText());
        parameters.put("noOfItems",totalItemsLabel.getText());
        parameters.put("amountGiven",balanceCalcTxt.getText());
        parameters.put("balanceAmt",balanceAmtLabel.getText());*/

        try{
            InputStream input = new FileInputStream(new File("jasperReport/Blank_Letter.jrxml"));
            JasperDesign jasperDesign = JRXmlLoader.load(input);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,parameters,new JREmptyDataSource());
            
            
            
            JasperPrintManager.printReport(jasperPrint,true);


        }catch (IOException | JRException e){
            e.printStackTrace();
            e.getCause();
        }

    }

    public void savebtnOnAction(ActionEvent event)
    {
        save();
    }
    public void save()
    {

        if (roundOffLbl.getText().isBlank()==false)
        {

            String getinvNum = "select ID from InvoiceNumbers order by ID DESC LIMIT 1";
            Double noOfProducts = 0.0;
            List<Double> noOfProductsList = new ArrayList<>();
            

            for (Product item : tableView.getItems())
            {
                noOfProductsList.add(qtyCol.getCellObservableValue(item).getValue());
            }
            for(Double d:noOfProductsList)
                noOfProducts = noOfProducts + d;

            try{
                Statement statement = connectDB.createStatement();
                ResultSet invNumSet = statement.executeQuery(getinvNum);
                Integer invValue = 0;
                while (invNumSet.next())
                {
                    invValue = invNumSet.getInt("ID");
                }
                LocalDate currentDate = LocalDate.now();
                invValue=invValue+1;
                String tableName = "INV"+invValue+currentDate.getMonthValue()+currentDate.getYear();


                String create = "create table "+tableName+" (InvoiceID varchar(200),ProductID int,Description varchar(200),Price double,Quantity int (50),GSTP int(10),GSTAmt double,Amount double,SubTotal double" +
                        ",TotalGSTAmount double,TotalAmount double,CustomerName varchar(50),CustomerNumber varchar(50),CustomerAddress varchar(400),ProductsSold int,Balance double)";

                statement.executeUpdate(create);
                ObservableList<Product> allProducts1;
                allProducts1 = tableView.getItems();
                for (Product res : allProducts1)
                {
                    String createInvoice = "insert into "+tableName+" (InvoiceID,ProductID,Description,Price,Quantity,GSTP,GSTAmt,Amount,SubTotal,TotalAmount,TotalGSTAmount,CustomerName," +
                            "CustomerNumber,CustomerAddress,ProductsSold,Balance) values ('"+tableName+"','"+res.getProductID()+"','"+res.getProductName()+"','"+res.getProductPrice()+"','"+res.getQty()+"','"+res.getProductGSTPercentage()+"'," +
                            "'"+res.getProductGST()+"','"+res.getAmount()+"','"+subTotalLbl.getText()+"','"+roundOffLbl.getText()+"','"+totalGstAmtLbl.getText()+"'," +
                            "'"+customerNameTxt.getText()+"','"+customerNumberTxt.getText()+"','"+customerAddressTxt.getText()+"','"+noOfProducts+"','"+balanceAmtLabel.getText()+"')";

                    statement.executeUpdate(createInvoice);
                }
                String storeInvNum = "insert into InvoiceNumbers (InvNumber,CustomerName,TotalAmount, ProductsSold)" +
                        "values('"+tableName+"','"+customerNameTxt.getText()+"','"+roundOffLbl.getText()+"','"+noOfProducts+"')";
                statement.executeUpdate(storeInvNum);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Print");
                String s = "Do you want printed report...";
                alert.setContentText(s);
                Optional<ButtonType> result = alert.showAndWait();
                if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
                    print();
                }
                updateStock();

                reset();
                totalAmountLbl.setText("");
                totalGstAmtLbl.setText("");
                roundOffLbl.setText("");
                customerNameTxt.setText("");
                customerAddressTxt.setText("");
                customerNumberTxt.setText("");
                invValue=invValue+1;
                tableName = "INV"+invValue+currentDate.getMonthValue()+currentDate.getYear();
                invoiceNoLbl.setText(tableName);


                //String createInvoice = "insert into "+tableName+" (ProductID,Description,Price,Quantity,GSTP,GSTAmt,Amount,SubTotal,TotalGSTAmount,TotalAmount,CustomerName," +
                //      "CustomerNumber,CustomerAddress) values ()";

            }catch (Exception e)
            {
                e.printStackTrace();
                e.getCause();
            }

        }

        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            String s = "Submit the form";
            alert.setContentText(s);
            alert.showAndWait();
        }

    }
    public void updateStock()
    {
        String getStocks = "";
        try
        {
            Integer curID;
            Integer curStock;
            Integer curDBStocks;

            for (Product item : tableView.getItems())
            {
                curID = productIDCol.getCellObservableValue(item).getValue();
                curStock = qtyCol.getCellObservableValue(item).getValue().intValue();
                getStocks = "update productDetail set stocks = stocks-'"+curStock+"' where ID = '"+curID+"' ";
                Statement statement = connectDB.createStatement();
                statement.executeUpdate(getStocks);
            }

        }catch (Exception e)
        {
            e.printStackTrace();
            e.getCause();
        }

    }

    public void resetBtnOnAction(ActionEvent event)
    {
        reset();
    }
    public void reset()
    {
        tableView.getItems().clear();
        subTotalLbl.setText("0.0");
        productNameTxt.setText("");
        qtyTxt.setText("");
        productNameTxt.requestFocus();
    }



}
