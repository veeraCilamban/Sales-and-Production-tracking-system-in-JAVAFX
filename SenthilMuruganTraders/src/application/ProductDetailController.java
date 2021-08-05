package application;
import javafx.application.Platform;
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

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Optional;
import java.util.ResourceBundle;

public class ProductDetailController implements Initializable {

    @FXML
    private AnchorPane mainProductAnchorPane;
    @FXML
    private TableView<ProductMgt> productTableView;
    @FXML
    private TableColumn<ProductMgt , Integer> productIDCol;
    @FXML
    private TableColumn<ProductMgt , String> productNameCol;
    @FXML
    private TableColumn<ProductMgt , Double> priceCol;
    @FXML
    private TableColumn<ProductMgt , Integer> gstCol;
    @FXML
    private TableColumn<ProductMgt , Integer> stocksCol;
    @FXML
    private TableColumn<ProductMgt , String> dateCol;

    @FXML
    private TextField productNameTxt;
    @FXML
    private TextField priceTxt;
    @FXML
    private TextField stocksTxt;
    @FXML
    private TextField gstTxt;
    @FXML
    private TextField updateProductNameTxt;
    @FXML
    private TextField updatePriceTxt;
    @FXML
    private TextField updateStocksTxt;
    @FXML
    private TextField updateGSTTxt;
    @FXML
    private  TextField searchTxt;

    @FXML
    private Button searchBtn;
    @FXML
    private Button showAllBtn;
    @FXML
    private Button addBtn;
    @FXML
    private Button showBtn;
    @FXML
    private Button updateStocks;

    @FXML
    private Label productCountLabel;
    @FXML
    private Label totalStocksLabel;



    DBConnection connectNow = new DBConnection();
    Connection connectDB = connectNow.getConnection();
    Integer getID;

    public ProductDetailController() throws SQLException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        mainProductAnchorPane.setFocusTraversable(false);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                searchTxt.requestFocus();
            }
        });
        mainProductAnchorPane.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (saveShortcut.match(event)) {
                    add();
                }
            }
        });
        mainProductAnchorPane.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (showAllShortcut.match(event)) {
                    showAll();
                }
            }
        });
        mainProductAnchorPane.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent k) {
                if (k.getCode().equals(KeyCode.ENTER)) {
                    show();
                }
            }
        });
        productTableView.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent k) {
                if (k.getCode().equals(KeyCode.DELETE)) {
                    try {
                        delete();
                    } catch (SQLException sqlException) {
                        sqlException.printStackTrace();
                    }
                }
            }
        });
        searchTxt.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent k) {
                if (k.getCode().equals(KeyCode.ENTER)) {
                    search();
                }
            }
        });
        productIDCol.setCellValueFactory( new PropertyValueFactory<>("ProductID"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("ProductName"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("ProductPrice"));
        gstCol.setCellValueFactory(new PropertyValueFactory<>("Gst"));
        stocksCol.setCellValueFactory(new PropertyValueFactory<>("Stocks"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("Date"));


        String getAllProduct = "select * from productDetail";
        try
        {
            Statement statement = connectDB.createStatement();
            ResultSet allProductQuerySet =statement.executeQuery(getAllProduct);
            Integer count =0;
            Integer totalStocks = 0;

            while (allProductQuerySet.next())
            {
                Integer product_ID = allProductQuerySet.getInt("ID");
                String product_name = allProductQuerySet.getString("ProductName");
                Double price = allProductQuerySet.getDouble("productPrice");
                Integer productGst = allProductQuerySet.getInt("GST");
                Integer stocks = allProductQuerySet.getInt("stocks");
                totalStocks = totalStocks + stocks;
                String date = new SimpleDateFormat("dd/MM/yyyy").format(allProductQuerySet.getDate("date"));

                ProductMgt productMgt = new ProductMgt(product_ID,product_name,price,productGst,stocks,date);
                productTableView.getItems().add(productMgt);
                count = count+1;
            }
            productCountLabel.setText(String.valueOf(count));
            totalStocksLabel.setText(String.valueOf(totalStocks));
        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }

    }
    public static final KeyCombination saveShortcut = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_ANY);
    public static final KeyCombination showAllShortcut = new KeyCodeCombination(KeyCode.A, KeyCombination.CONTROL_ANY);


    public void showAllBtnOnAction(ActionEvent event)
    {
        showAll();
    }
    public void showAll()
    {
        productTableView.getItems().clear();
        String getAllProduct = "select * from productDetail";
        try
        {
            Statement statement = connectDB.createStatement();
            ResultSet allProductQuerySet =statement.executeQuery(getAllProduct);

            while (allProductQuerySet.next())
            {
                Integer product_ID = allProductQuerySet.getInt("ID");
                String product_name = allProductQuerySet.getString("ProductName");
                Double price = allProductQuerySet.getDouble("productPrice");
                Integer productGst = allProductQuerySet.getInt("GST");
                Integer stocks = allProductQuerySet.getInt("stocks");
                String date = new SimpleDateFormat("dd/MM/yyyy").format(allProductQuerySet.getDate("date"));

                ProductMgt productMgt = new ProductMgt(product_ID,product_name,price,productGst,stocks,date);
                productTableView.getItems().add(productMgt);

            }
        }catch (Exception e){
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
        productTableView.getItems().clear();
        String searchVal = searchTxt.getText();
        String getSearchData = "SELECT * FROM productDetail WHERE ProductName like '%"+searchVal+"%' or ID = '"+searchVal+"'";

        try
        {
            Statement statement = connectDB.createStatement();
            ResultSet searchSet = statement.executeQuery(getSearchData);
            if (!searchSet.isBeforeFirst())
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Data not matched");
                String s = "Incorrect name or ID";
                alert.setContentText(s);
                alert.showAndWait();
            }
            else
            {
                while (searchSet.next())
                {
                    Integer product_ID = searchSet.getInt("ID");
                    String product_name = searchSet.getString("ProductName");
                    Double price = searchSet.getDouble("productPrice");
                    Integer productGst = searchSet.getInt("GST");
                    Integer stocks = searchSet.getInt("stocks");
                    String date = new SimpleDateFormat("dd/MM/yyyy").format(searchSet.getDate("date"));
                    ProductMgt productMgtSearch = new ProductMgt(product_ID,product_name,price,productGst,stocks,date);
                    productTableView.getItems().add(productMgtSearch);
                }
            }

        }catch (Exception e){

        }
    }

    public void addBtnOAction(ActionEvent event)
    {
        add();
    }
    public void add()
    {
        if (productNameTxt.getText().isBlank()==false && priceTxt.getText().isBlank() == false && gstTxt.getText().isBlank()==false) {
            String product_name = productNameTxt.getText();
            Double price = Double.valueOf(priceTxt.getText());
            Integer gst = Integer.valueOf(gstTxt.getText());

            String storeData = "insert into productDetail (ProductName,productPrice,GST) values ('" + product_name + "','" + price + "','" + gst + "')";
            try {
                Statement statement = connectDB.createStatement();
                statement.executeUpdate(storeData);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("ADD");
                alert.setHeaderText("Product " + product_name + " has been added successfully");

                alert.show();
                productNameTxt.setText("");
                priceTxt.setText("");
                gstTxt.setText("");
                showAll();
                productNameTxt.requestFocus();

            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                String s = "Enter Valid Data";
                alert.setContentText(s);
                alert.showAndWait();
            }
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            String s = "Enter Valid Data";
            alert.setContentText(s);
            alert.showAndWait();
        }
    }

    public void showBtnOnAction(ActionEvent event)
    {
        show();
    }
    public void show()
    {
        try
        {
            ObservableList<ProductMgt> singleProduct;
            singleProduct = productTableView.getSelectionModel().getSelectedItems();

            for (ProductMgt res : singleProduct)
            {
                updateProductNameTxt.setText(res.getProductName());
                updatePriceTxt.setText(String.valueOf(res.getProductPrice()));
                updateStocksTxt.setText(String.valueOf(res.getStocks()));
                updateGSTTxt.setText(String.valueOf(res.getGst()));
                getID = res.getProductID();
            }
        }catch (Exception e)
        {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            String s = "Select Product from table";
            alert.setContentText(s);
            alert.showAndWait();

        }
    }

    public void updateBtnOnAction(ActionEvent event) throws SQLException {

        update();

    }
    public void update() throws SQLException {
        if (getID == null && updateProductNameTxt.getText().isBlank() && updatePriceTxt.getText().isBlank() && updateStocksTxt.getText().isBlank() && updateGSTTxt.getText().isBlank())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            String s = "Select Product from table and click show";
            alert.setContentText(s);
            alert.showAndWait();
        }
        else
        {
            String product_name = updateProductNameTxt.getText();
            Double price = Double.valueOf(updatePriceTxt.getText());
            Integer gst = Integer.valueOf(updateGSTTxt.getText());


            String update = "update productDetail set ProductName='"+product_name+"',productPrice = '"+price+"',GST='"+gst+"' where ID = '"+getID+"' ";
            Statement statement = connectDB.createStatement();
            statement.executeUpdate(update);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Update");
            alert.setHeaderText("Product detail has been updated successfully");
            //String s ="This is an example of JavaFX 8 Dialogs... ";
            //alert.setContentText(s);
            alert.show();
            updateProductNameTxt.setText("");
            updatePriceTxt.setText("");
            updateStocksTxt.setText("");
            updateGSTTxt.setText("");
            showAll();
        }
    }
    public void updateStockBtnOnAction(ActionEvent event) throws SQLException {
        updateStock();
    }
    public void updateStock() throws SQLException {
        if (getID == null && updateProductNameTxt.getText().isBlank() && updatePriceTxt.getText().isBlank() &&updateStocksTxt.getText().isBlank()  && updateGSTTxt.getText().isBlank())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            String s = "Select Product from table and click show";
            alert.setContentText(s);
            alert.showAndWait();
        }
        else
        {
            Integer stocks = Integer.valueOf(updateStocksTxt.getText());
            String product_name = updateProductNameTxt.getText();
            String updateStocks = "update productDetail set stocks=stocks+'"+stocks+"' where ID = '"+getID+"' ";
            String addTodayStocks = "insert into TodayProduction (TP_ID,TodayStocks,ProductName) values ('"+getID+"','"+stocks+"','"+product_name+"')";
            Statement statement = connectDB.createStatement();
            statement.executeUpdate(updateStocks);
            statement.executeUpdate(addTodayStocks);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Update");
            alert.setHeaderText("Stock of "+product_name+" has been updated successfully");
            //String s ="This is an example of JavaFX 8 Dialogs... ";
            //alert.setContentText(s);
            alert.show();
            showAll();
            updateProductNameTxt.setText("");
            updatePriceTxt.setText("");
            updateGSTTxt.setText("");
            updateStocksTxt.setText("");

        }


    }
    public void deleteBtnOnAction(ActionEvent event) throws SQLException {
        delete();
    }
    public void delete() throws SQLException {

        ObservableList<ProductMgt> selectedProduct;
        selectedProduct = productTableView.getSelectionModel().getSelectedItems();
        Integer delProductID = null;
        String delProductName = "";
        for (ProductMgt res : selectedProduct)
        {
            delProductID = res.getProductID();
            delProductName = res.getProductName();
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete");
        String s = "Do you want to delete the product "+delProductName+ " permanently";
        alert.setContentText(s);
        Optional<ButtonType> result = alert.showAndWait();
        if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
            String delProductQuery = "delete from productDetail where ID = '"+delProductID+"'";
            Statement statement = connectDB.createStatement();
            statement.executeUpdate(delProductQuery);
            showAll();
        }
    }


}
