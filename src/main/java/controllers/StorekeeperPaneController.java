package controllers;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class StorekeeperPaneController {

  public TextField tfDeliverer;
  public TextField tfCode;
  public TextField tfAmount;
  public Label lError;
  public Label lSuccess;

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

  public void bLogoutClick(ActionEvent event) {
    controller.setLoginPane();
  }

  public void bAddClick(ActionEvent event) {
    lError.setVisible(false);
    lSuccess.setVisible(false);

    String deliverer = tfDeliverer.getText();
    String code = tfCode.getText();
    String amount = tfAmount.getText();

    if(!checkFormat(amount)) {
      lError.setVisible(true);
      return;
    }

    lSuccess.setVisible(true);
  }

  public void bNewProductClick(ActionEvent event) {
    setNewProductPane();
  }

  private void setNewProductPane() {
    FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxmlFiles/NewProductPane.fxml"));
    AnchorPane newProductPane = null;
    try {
      newProductPane = loader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }
    NewProductPaneController newProductController = loader.getController();
    newProductController.setController(controller);
    newProductController.setLoginController(loginController);
    controller.setPane(newProductPane);
  }

  private boolean checkFormat(String check) {
    int i;
    try {
      i = Integer.parseInt(check);
    } catch (NumberFormatException e) {
      return false;
    }
    return true;
  }
}
