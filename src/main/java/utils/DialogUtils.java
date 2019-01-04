package utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class DialogUtils {

  public static Optional<ButtonType> confirmationDialog(String title, String message){

    Alert informationAlert = new Alert(Alert.AlertType.CONFIRMATION);
    informationAlert.setTitle(title);
    informationAlert.setHeaderText(message);
    informationAlert.setX(730);
    informationAlert.setY(420);
    Optional<ButtonType> result = informationAlert.showAndWait();
    return result;
  }
}
