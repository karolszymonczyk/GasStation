package controllers;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import workers.Storekeeper;

public class NewProductPaneController {

  public TextField tfName;
  public TextField tfCode;
  public TextField taAmount;
  public Button bCreate;
  public Label lError;
  public Label lSucces;
  public TextField tfPrice;
  public TextField tfTax;

  private Storekeeper storekeeper;

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
    loginController.setStorekeeperPane();
  }

  public void bCreateClick(ActionEvent event) {
    lError.setVisible(false);

    String name = tfName.getText();
    String code = tfCode.getText();
    String price = tfPrice.getText();
    String tax = tfTax.getText();
    String amount = taAmount.getText();

    if(!checkFormat(price) || !checkFormat(amount)) {
      lError.setVisible(true);
      return;
    }

    storekeeper.addNewProduct(Integer.parseInt(code),name,Float.parseFloat(price),Float.parseFloat(tax),Integer.parseInt(amount),"ZMIENICwWYWOLANIUmetody");

    lSucces.setVisible(true);
    setDisbledPane();
  }

  private void setDisbledPane() {
    tfName.setDisable(true);
    tfCode.setDisable(true);
    tfPrice.setDisable(true);
    tfTax.setDisable(true);
    taAmount.setDisable(true);
    bCreate.setDisable(true);
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

  public void setStorekeeper(Storekeeper storekeeper) {
    this.storekeeper = storekeeper;
  }
}
