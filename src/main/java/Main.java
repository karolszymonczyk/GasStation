import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;



public class Main extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  public void start(Stage primaryStage) throws Exception {



    Parent mainPane = FXMLLoader.load(this.getClass().getResource("/fxmlFiles/MainPane.fxml"));

    primaryStage.setTitle("Store");
    primaryStage.setScene(new Scene(mainPane, 850, 650));
    primaryStage.setResizable(false);

    primaryStage.getIcons().add(new Image(this.getClass().getResourceAsStream("/images/logo.png")));

    primaryStage.setX(500);
    primaryStage.setY(200);
    primaryStage.show();
  }
}
