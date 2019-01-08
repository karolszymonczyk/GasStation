package controllers;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import workers.Seller;

public class CardPaneController {

  public TextField tfName;
  public TextField tfSurname;
  public TextField taNIP;
  public Label lError;
  public Label lSuccess;
  public Button bCreate;
  private MainController controller;
  private LoginPaneController loginController;
  private Seller seller;
  private String customer;
  private SellerPaneController sellerController;

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

    loginController.setSellerPane(customer);
    sellerController.updateBillList(seller.getActiveBill());
  }

  public void bCreateClick(ActionEvent event) {

    lError.setVisible(false);

    String name = tfName.getText();
    String surname = tfSurname.getText();
    String sNIP = taNIP.getText();

    if (!checkFormat(sNIP)) {
      lError.setVisible(true);
      return;
    }

    int iNIP = Integer.parseInt(sNIP);

    seller.addNewCustomer(name, surname, iNIP);

    lSuccess.setVisible(true);
    setDisabledPane();
  }

  private void setDisabledPane() {
    tfName.setDisable(true);
    tfSurname.setDisable(true);
    taNIP.setDisable(true);
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

  void setSeller(Seller seller) {
    this.seller = seller;
  }

  void setCustomer(String customer) {
    this.customer = customer;
  }

  void setSellerController(SellerPaneController sellerController) {
    this.sellerController = sellerController;
  }
}
