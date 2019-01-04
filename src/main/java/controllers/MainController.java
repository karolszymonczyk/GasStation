package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class MainController {

  @FXML
  private Pane mainPane;

  @FXML
  public void initialize() {
    setLoginPane();
  }
//
  public void setLoginPane() {
    FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxmlFiles/LoginPane.fxml"));
    Pane logPane = null;
    try {
      logPane = loader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }
    LoginPaneController lController = loader.getController();
    lController.setController(this);
    setPane(logPane);
  }

  public void setPane(Pane pane) {
    mainPane.getChildren().clear();
    mainPane.getChildren().add(pane);
  }
}
