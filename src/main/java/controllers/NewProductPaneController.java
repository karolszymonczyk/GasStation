package controllers;

import elements.ProductForDeliver;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import workers.Storekeeper;

import java.sql.SQLException;

public class NewProductPaneController {

  public TextField tfName;
  public TextField tfCode;
  public TextField taAmount;
  public Button bCreate;
  public Label lError;
  public Label lSucces;
  public TextField tfPrice;
  public TextField tfTax;
  TextField taDeliverer;

  private Storekeeper storekeeper;

  private MainController controller;
  private LoginPaneController loginController;
  public String deliverer;

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

  public void setDeliverer(String deliverer) {
    this.deliverer = deliverer;
  }

  public void bBackClick(ActionEvent event) {
    loginController.setStorekeeperPane(deliverer);
  }

  public void bCreateClick(ActionEvent event) {
    lError.setVisible(false);

    String name = tfName.getText();
    String code = tfCode.getText();
    String price = tfPrice.getText();
    String tax = tfTax.getText();
    String amount = taAmount.getText();
    String deliverer = taDeliverer.getText();

    if (!checkFormat(price) || !checkFormat(amount)) {
      lError.setVisible(true);
      return;
    }


    if (!storekeeper.isTranactionStarted()) {
      storekeeper.setTranactionStarted(true);

      try {
        storekeeper.getConnection().setAutoCommit(false);
        storekeeper.createDelivery();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }

    int amountInt = Integer.parseInt(amount);
    int codeInt = Integer.parseInt(code);

    storekeeper.addNewProduct(Integer.parseInt(code), name, Float.parseFloat(price), Float.parseFloat(tax), Integer.parseInt(amount), deliverer);

    ProductForDeliver productForDeliver = new ProductForDeliver(storekeeper.getProductName(codeInt), code, amountInt);
    storekeeper.addDeliveryProduct(productForDeliver);

    lSucces.setVisible(true);
    setDisbledPane();
  }

  void setDisbledPane() {
    tfName.setDisable(true);
    tfCode.setDisable(true);
    tfPrice.setDisable(true);
    tfTax.setDisable(true);
    taAmount.setDisable(true);
    taDeliverer.setDisable(true);
    bCreate.setDisable(true);
  }

  private boolean checkFormat(String check) {
    try {
      Integer.parseInt(check);
    } catch (NumberFormatException e) {
      return false;
    }
    return true;
  }

  public void setStorekeeper(Storekeeper storekeeper) {
    this.storekeeper = storekeeper;
  }
}
