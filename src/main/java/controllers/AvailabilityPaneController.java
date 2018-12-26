package controllers;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

public class AvailabilityPaneController {

  public TableView tvProducts;
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

  public void setTvProducts(TableView tvProducts) {
    this.tvProducts = tvProducts;
  }

  public void bBackClick(ActionEvent event) {
    loginController.setSellerPane();
  }
}
