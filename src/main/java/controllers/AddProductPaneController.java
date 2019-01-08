package controllers;

import elements.Product;
import elements.ProductView;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import workers.Manager;

//
public class AddProductPaneController {
  public TextField tfName;
  public TextField tfCode;
  public TextField taAmount;
  public Button bCreate;
  public Label lError;
  public Label lSucces;
  public TextField tfPrice;
  public TextField tfTax;
//  public TextField taDeliverer;

  private Manager manager;

  private MainController controller;
//  private LoginPaneController loginController;
  private ManagerPaneController managerController;

  @FXML
  public void initialize() {
    Application.setUserAgentStylesheet(Application.STYLESHEET_CASPIAN);
  }

  public void setController(MainController controller) {
    this.controller = controller;
  }

  public void setManagerController(ManagerPaneController managerController) {
    this.managerController = managerController;
  }

  public void bBackClick(ActionEvent event) {
    manager.downloadProducts();
//  managerController.getManager().downloadProducts();
    managerController.setViewProductsPane();
    manager.downloadProducts();


  }

  public void bCreateClick(ActionEvent event) {

    lError.setVisible(false);

    String name = tfName.getText();
    String code = tfCode.getText();
    String price = tfPrice.getText();
    String tax = tfTax.getText();
    String amount = taAmount.getText();
//    String deliverer = taDeliverer.getText();

    if(!checkFormat(price) || !checkFormat(amount)) {
      lError.setVisible(true);
      return;
    }

    manager.addProduct(name,Float.parseFloat(price),Float.parseFloat(tax),Integer.parseInt(code),Integer.parseInt(amount));

    //ProductView product = new ProductView(code,name,Double.parseDouble(price),Double.parseDouble(tax),Integer.parseInt(amount));

    lSucces.setVisible(true);
    setDisbledPane();
  }

  private void setDisbledPane() {
    tfName.setDisable(true);
    tfCode.setDisable(true);
    tfPrice.setDisable(true);
    tfTax.setDisable(true);
    taAmount.setDisable(true);
//    taDeliverer.setDisable(true);
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

  public void setManager(Manager manager) {
    this.manager=manager;
  }
}