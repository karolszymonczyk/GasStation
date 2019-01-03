package controllers;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

//TODO status working unemployed

//TODO job seller, storekeeper, manager

public class ViewWorkersPaneController {

  private MainController controller;
  private LoginPaneController loginController;

  @FXML
  public void initialize() {
    Application.setUserAgentStylesheet(Application.STYLESHEET_CASPIAN);
  }

  public void setController(MainController controller) {
    this.controller = controller;
  }

  public void setLoginController(LoginPaneController LoginController) {
    this.loginController = LoginController;
  }

  public void bBackClick(ActionEvent event) {
    loginController.setManagerPane();
  }
}

//TODO 1.Tworzymy bill (ma seller id i status: active lub closed)
//TODO 2.Insertuemy produkty do danego billa