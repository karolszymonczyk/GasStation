import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//TODO rada na przyszłość XD jak nie działa to zmienić wersje w modules na 9 XD

//TODO jak kasjer dodaje produkty do rachunku to w javie dodajemy je do tablicy a potem tablice parsujemy na stringa (product id oddzielone przecinkami) i to wysyøamy do mysql;

//TODO DODAC W TRIGERZE W DELIVERY ZE JAK NOWEGO PRODUKTU NIE MA W TABELI TO TWORZY NOWY PRODUKT

//TODO DOPISAC TRIGGER DO BALANCE CONTROLL NA KARCIE

//TODO TRANSAKCJA ZACZYNA SIĘ W MOMENCIE GDY zaczynamy dodawać produkty, w tym momencie nie ma opcji stwórz kartę, sprawdż dostępność, transakcje można anulować przez klikniecie przycisku(jakiegoś nowego)

public class Main extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  public void start(Stage primaryStage) throws Exception {



    Parent mainPane = FXMLLoader.load(this.getClass().getResource("/fxmlFiles/MainPane.fxml"));

    primaryStage.setTitle("GasStation");
    primaryStage.setScene(new Scene(mainPane, 850, 650));

    primaryStage.setX(500);
    primaryStage.setY(200);
    primaryStage.show();
  }
}
