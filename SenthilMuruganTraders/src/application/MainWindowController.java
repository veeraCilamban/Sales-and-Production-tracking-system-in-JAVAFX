package application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {
    @FXML
    private ScrollBar mainScrollBar;
    @FXML
    private BorderPane mainPane;
    //ImageView
    @FXML
    private ImageView brandImageView;
    @FXML
    private ImageView productReportIcon;
    @FXML
    private ImageView salesIcon;
    @FXML
    private ImageView todayProductionIcon;
    @FXML
    private ImageView invoiceReportIcon;

    private Pane view;

    @FXML
    private Button testBtn;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        
        File brandFile = new File("image/BrandLogo1.png");
        Image brandImage = new Image(brandFile.toURI().toString());
        brandImageView.setImage(brandImage);

        File productFile = new File("image/ProductReportIcon.png");
        Image productImage = new Image(productFile.toURI().toString());
        productReportIcon.setImage(productImage);

        File salesFile = new File("image/salesIcon.png");
        Image salesImage = new Image(salesFile.toURI().toString());
        salesIcon.setImage(salesImage);

        File todayProductionFile = new File("image/TodayProductionIcon.png");
        Image todayProductionImage = new Image(todayProductionFile.toURI().toString());
        todayProductionIcon.setImage(todayProductionImage);

        File invoiceReportFile = new File("image/InvoiceReportIcon.png");
        Image invoiceReportImage = new Image(invoiceReportFile.toURI().toString());
        invoiceReportIcon.setImage(invoiceReportImage);

        try {
            view = FXMLLoader.load(getClass().getResource("sales.fxml"));
            mainPane.setCenter(view);

        } catch (IOException e) {
            e.printStackTrace();
            e.getCause();
        }


    }
    public void todayProductionOnAction(ActionEvent event)
    {
        try {
            view = FXMLLoader.load(getClass().getResource("todayProduction.fxml"));
            mainPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
            e.getCause();
        }


    }

    public void productReportSwitchBtnOnAction(ActionEvent event)
    {
        try {
            view = FXMLLoader.load(getClass().getResource("productReport.fxml"));
            mainPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
            e.getCause();
        }


    }
    public void salesSwitchBtnOnAction(ActionEvent event)
    {
        try {
            view = FXMLLoader.load(getClass().getResource("sales.fxml"));
            mainPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
            e.getCause();
        }

    }
    public void invoiceReportBtnOnAction(ActionEvent event)
    {
        try
        {
            view = FXMLLoader.load(getClass().getResource("invoiceReport.fxml"));
            mainPane.setCenter(view);
        }catch (IOException e)
        {
            e.printStackTrace();
            e.getCause();
        }
    }

}
