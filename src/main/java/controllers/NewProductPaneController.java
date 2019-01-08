package controllers;

import elements.ProductForDeliver;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import utils.ErrorUtils;
import workers.Storekeeper;

import java.sql.SQLException;
import java.sql.Savepoint;

public class NewProductPaneController implements ErrorUtils {

  public TextField tfName;
  public TextField tfCode;
  public TextField taAmount;
  public Button bCreate;
  public Label lError;
  public Label lSuccess;
  public TextField tfPrice;
  public TextField tfTax;

  private Storekeeper storekeeper;

  private MainController controller;
  private LoginPaneController loginController;
  public String deliverer;

  Savepoint delete;

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
    loginController.setStorekeeperPane(deliverer,delete);
  }

  public void bCreateClick(ActionEvent event) {

    lError.setVisible(false);

    String name = tfName.getText();
    String code = tfCode.getText();
    String price = tfPrice.getText();
    String tax = tfTax.getText();
    String amount = taAmount.getText();
    
    if(name.equals("") || code.equals("") || price.equals("") ||
            tax.equals("") || amount.equals("")) {
      lError.setVisible(true);
      return;
    }

    if(!ErrorUtils.checkInt(code) || !ErrorUtils.checkInt(amount)) {
      lError.setVisible(true);
      return;
    }

    if(!ErrorUtils.checkFloat(price) || !ErrorUtils.checkFloat(tax)) {
      lError.setVisible(true);
      return;
    }

    if (!storekeeper.isTransactionStarted()) {
      storekeeper.setTransactionStarted(true);

      try {
        storekeeper.getConnection().setAutoCommit(false);
        storekeeper.createDelivery();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }

    int amountInt = Integer.parseInt(amount);
    int codeInt = Integer.parseInt(code);

    storekeeper.addNewProduct(Integer.parseInt(code), name, Float.parseFloat(price), Float.parseFloat(tax), Integer.parseInt(amount));

    ProductForDeliver productForDeliver = new ProductForDeliver(storekeeper.getProductName(codeInt), code, amountInt);
    storekeeper.addDeliveryProduct(productForDeliver);

    lSuccess.setVisible(true);
    setDisabledPane();
  }

  void setDisabledPane() {
    tfName.setDisable(true);
    tfCode.setDisable(true);
    tfPrice.setDisable(true);
    tfTax.setDisable(true);
    taAmount.setDisable(true);
    bCreate.setDisable(true);
  }

  void setStorekeeper(Storekeeper storekeeper) {
    this.storekeeper = storekeeper;
  }

  public void setDelete(Savepoint delete) {
    this.delete = delete;
  }
}
