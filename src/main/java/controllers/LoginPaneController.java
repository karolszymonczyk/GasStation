package controllers;

import javafx.application.Application;
import javafx.fxml.FXML;

public class LoginPaneController {

  private MainController controller;

  @FXML
  public void initialize() {
    Application.setUserAgentStylesheet(Application.STYLESHEET_CASPIAN);

  }

  public void setController(MainController controller) {
    this.controller = controller;
  }
}
