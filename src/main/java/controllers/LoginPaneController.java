package controllers;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class LoginPaneController {

  private MainController controller;

  private String login, password;

  @FXML
  private TextField tLogin, tPassword;

  @FXML
  private Button bLogin;

  @FXML
  public void initialize() {
    Application.setUserAgentStylesheet(Application.STYLESHEET_CASPIAN);

  }

  public void setController(MainController controller) {
    this.controller = controller;
  }

  public void bLoginClick(ActionEvent event) {
    login = tLogin.getText();
    password = tPassword.getText();

    System.out.println(login);
    System.out.println(password);
  }
}
