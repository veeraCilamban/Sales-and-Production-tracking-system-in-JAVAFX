package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) throws Exception{
		Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setScene(new Scene(root,672,400));
		primaryStage.show();
    }
	
	public static void main(String[] args) {
		launch(args);
	}
}
