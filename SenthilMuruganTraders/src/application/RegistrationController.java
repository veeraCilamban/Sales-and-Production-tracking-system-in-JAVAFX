package application;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


import java.sql.Connection;
import java.sql.Statement;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class RegistrationController implements Initializable {


    @FXML
    private ImageView registerBrandView;
    @FXML
    private ImageView registerIconView;
    @FXML
    private TextField nameTxt;
    @FXML
    private TextField unameTxt;
    @FXML
    private TextField ageTxt;
    @FXML
    private PasswordField pwdTxt;
    @FXML
    private PasswordField cPwdTxt;
    @FXML
    private Button registerBtn;
    @FXML
    private Button loginBtn;
    @FXML
    private Button closeBtn;
    @FXML
    private Label errorMessageLabel;
    @FXML
    private Label successLabel;

    @FXML
    private BorderPane mainPane;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File regBrandingFile = new File("image/BrandLogo1.png");
        Image regBrandingImage = new Image(regBrandingFile.toURI().toString());
        registerBrandView.setImage(regBrandingImage);

        File regFile = new File("image/Register-Icon.png");
        Image regImage = new Image(regFile.toURI().toString());
        registerIconView.setImage(regImage);
        mainPane.setFocusTraversable(false);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                nameTxt.requestFocus();
            }
        });
        mainPane.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent k) {
                if (k.getCode().equals(KeyCode.ENTER)) {
                    register();
                }
            }
        });

    }

    public void registerBtnOnAction(ActionEvent event)
    {
        register();
    }
    public void register()
    {
        //errorLabel.setText("Your authendicated");
        if(nameTxt.getText().isBlank()) {
            errorMessageLabel.setText("Name Field is required !");
        }
        else if (unameTxt.getText().isBlank()){
            errorMessageLabel.setText("User name is required !");
        }
        else if (ageTxt.getText().isBlank()){
            errorMessageLabel.setText("Age is required ! ");
        }
        else{
            registerValidate();
        }
    }


    public void closeBtnOnAction(ActionEvent event) {
        Stage stage = (Stage) closeBtn.getScene().getWindow();
        stage.close();
    }

    public void registerValidate()
    {
        if (pwdTxt.getText().isBlank()==false && cPwdTxt.getText().isBlank()==false)
        {
            if (pwdTxt.getText().equals(cPwdTxt.getText()))
            {
                DBConnection connectNow = new DBConnection();
                Connection connectDB = connectNow.getConnection();

                String name = nameTxt.getText();
                String username = unameTxt.getText();
                String age = ageTxt.getText();
                String password = pwdTxt.getText();


                String addUser = "insert into users (Name,uname,password,age) values ('"+name+"','"+username+"','"+password+"','"+age+"')";

                try
                {
                    Statement statement = connectDB.createStatement();
                    statement.executeUpdate(addUser);
                    errorMessageLabel.setText("");
                    successLabel.setText("User has been successfully added.. :)");
                    nameTxt.setText("");
                    unameTxt.setText("");
                    ageTxt.setText("");
                    pwdTxt.setText("");
                    cPwdTxt.setText("");
                }catch (Exception e) {
                    e.printStackTrace();
                    e.getCause();
                }


            }
            else
            {
                errorMessageLabel.setText("password and confirm password should be same... ");
            }
        }
        else
        {
            errorMessageLabel.setText("Password and Confirm Password are required !");
        }
    }
}
