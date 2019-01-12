package controllers;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import utils.ErrorUtils;
import workers.Manager;


public class AddProductPaneController implements ErrorUtils{
  
  public TextField tfName;
  public TextField tfCode;
  public TextField taAmount;
  public Button bCreate;
  public Label lError;
  public Label lSucces;
  public TextField tfPrice;
  public TextField tfTax;

  private Manager manager;

  private MainController controller;
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

    if(name.equals("") || code.equals("") || price.equals("") ||
            tax.equals("") || amount.equals("")) {
      lError.setVisible(true);
      return;
    }

    if(!ErrorUtils.checkInt(code) || !ErrorUtils.checkInt(amount)) {
      lError.setVisible(true);
      return;
    }

    if(manager.searchForProductFromCode(Integer.parseInt(code)) || manager.searchForProductFromName(name.toLowerCase())){
      lError.setVisible(true);
      return;
    }

    if(!ErrorUtils.checkFloat(price) || !ErrorUtils.checkFloat(tax)) {
      lError.setVisible(true);
      return;
    }

    manager.addProduct(name,Float.parseFloat(price),Float.parseFloat(tax),Integer.parseInt(code),Integer.parseInt(amount));

    lSucces.setVisible(true);
    setDisabledPane();
  }

  private void setDisabledPane() {
    tfName.setDisable(true);
    tfCode.setDisable(true);
    tfPrice.setDisable(true);
    tfTax.setDisable(true);
    taAmount.setDisable(true);
    bCreate.setDisable(true);
  }

  public void setManager(Manager manager) {
    this.manager=manager;
  }
}
