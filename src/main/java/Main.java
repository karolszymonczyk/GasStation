import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//TODO rada na przyszłość XD jak nie działa to zmienić wersje w modules na 9 XD

//TODO DOPISAC TRIGGER DO BALANCE CONTROLL NA KARCIE

public class Main extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  public void start(Stage primaryStage) throws Exception {



    Parent mainPane = FXMLLoader.load(this.getClass().getResource("/fxmlFiles/MainPane.fxml"));

    primaryStage.setTitle("GasStation");
    primaryStage.setScene(new Scene(mainPane, 850, 650));
    primaryStage.setResizable(false);

    primaryStage.setX(500);
    primaryStage.setY(200);
    primaryStage.show();
  }
}
