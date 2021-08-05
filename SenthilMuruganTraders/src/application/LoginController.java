package application;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.stage.StageStyle;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.net.URL;



public class LoginController implements Initializable {

    @FXML
    private Button closeBtn;
    @FXML
    private Button loginBtn;
    @FXML
    private Label errorLabel;
    @FXML
    private Button createRegisterBtn;
    @FXML
    private ImageView brandingImageView;
    @FXML
    private ImageView lockView;
    @FXML
    private TextField unameTxtField;
    @FXML
    private PasswordField pwdTxtField;
    @FXML
    private BorderPane loginPane;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loginPane.setFocusTraversable(false);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                unameTxtField.requestFocus();
            }
        });
        loginPane.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent k) {
                if (k.getCode().equals(KeyCode.ENTER)) {
                    login();
                }
            }
        });

        File brandingFile = new File("image/BrandLogo1.png");
        Image brandingImage = new Image(brandingFile.toURI().toString());
        brandingImageView.setImage(brandingImage);

        File lockFile = new File("image/login.png");
        Image lockImage = new Image(lockFile.toURI().toString());
        lockView.setImage(lockImage);
    }

    public void loginBtnOnAction(ActionEvent event)
    {
        //errorLabel.setText("Your authendicated");
        login();
    }
    public void login()
    {
        if (unameTxtField.getText().isBlank() == false && pwdTxtField.getText().isBlank() == false)
        {
            validateLogin();
        }
        else
        {
            errorLabel.setText("Enter username and password");
        }
    }

    public void createRegisterBtnAction(ActionEvent event)
    {
        createRegisterStage();
    }

    public void cancelBtnOnAction(ActionEvent event)
    {
        Stage stage = (Stage) closeBtn.getScene().getWindow();
        stage.close();
    }

    public void validateLogin()
    {
        DBConnection connectNow = new DBConnection();
        Connection connectDB = connectNow.getConnection();
        String password = pwdTxtField.getText();


        String verifyLogin = "select count(1),Name from users where uname = '" + unameTxtField.getText() + "' and  password = '" + pwdTxtField.getText() +"'";

        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyLogin);

            while (queryResult.next()){
                if (queryResult.getInt(1) ==1 )
                {
                    errorLabel.setText("Welcome," + queryResult.getString("Name"));
                    try
                    {

                        Parent root = FXMLLoader.load(getClass().getResource("mainWindow.fxml"));
                        Stage salesStage = new Stage();
                        salesStage.initStyle(StageStyle.DECORATED);
                        salesStage.setScene(new Scene(root, 1676, 904));
                        salesStage.show();
                        Stage stage = (Stage) closeBtn.getScene().getWindow();
                        stage.close();

                    } catch (Exception e){
                        e.printStackTrace();
                        e.getCause();
                    }
                }
                else
                {
                    errorLabel.setText("Invalid username or password");
                }
            }

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }
    private static byte[] hashPassword(String password) {
        byte[] salt = new byte[16];
        byte[] hash = null;
        for (int i = 0; i < 16; i++) {
            salt[i] = (byte) i;
        }
        try {
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
            SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            hash = f.generateSecret(spec).getEncoded();

        } catch (NoSuchAlgorithmException nsale) {
            nsale.printStackTrace();

        } catch (InvalidKeySpecException ikse) {
            ikse.printStackTrace();
        }
        return hash;
    }

    public void createRegisterStage()
    {
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("register.fxml"));
            Stage registerStage = new Stage();
            registerStage.initStyle(StageStyle.UNDECORATED);
            registerStage.setScene(new Scene(root, 800, 600));
            registerStage.show();

        } catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

}
