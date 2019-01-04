package controllers;

import elements.ProductForDeliver;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import utils.DialogUtils;
import workers.Storekeeper;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.Optional;

public class StorekeeperPaneController {

  public TextField tfDeliverer;
  public TextField tfCode;
  public TextField tfAmount;
  public Label lError;
  public Label lSuccess;

  public TableView tvProducts;
  public TableColumn tvcName;
  public TableColumn tvcCode;
  public TableColumn tvcAmount;

  public Button bAdd;
  public Button bAddNewProduct;
  public Button bDelete;
  public Button bFinishDelivery;
  Storekeeper storekeeper;
  boolean transactionStarted = false;

  Savepoint delete;


  private MainController controller;
  private LoginPaneController loginController;

  String deliverer;

  @FXML
  public void initialize() {
    Application.setUserAgentStylesheet(Application.STYLESHEET_CASPIAN);
    tvcName.setCellValueFactory(new PropertyValueFactory<>("name"));
    tvcCode.setCellValueFactory(new PropertyValueFactory<>("code"));
    tvcAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
//    addToProductList();
  }

//  private void addToProductList() {
//    addProduct("Pomidor", "997", 1000);
//  }
//
//  private void addProduct(String name, String code, int amount) {
//    ProductForDeliver product = new ProductForDeliver(name, code, amount);
//    tvProducts.getItems().add(product);
//  }

  public void setController(MainController controller) {
    this.controller = controller;
  }

  public void setLoginController(LoginPaneController LoginController) {
    this.loginController = LoginController;
  }

  public void bLogoutClick(ActionEvent event) {
    if(logoutConfirmation()) {
      try {
        storekeeper.getConnection().rollback();
        storekeeper.getConnection().setAutoCommit(true);
      } catch (SQLException e) {
        System.out.println("rollback się nie wykonał ponieważ nie było aktywnej tranzakcji.");
      }
      storekeeper.deleteDelivery();
      controller.setLoginPane();
    }
  }

  public void bAddClick(ActionEvent event) {

    lError.setVisible(false);
    lSuccess.setVisible(false);

    deliverer = tfDeliverer.getText();
    String code = tfCode.getText();
    String amount = tfAmount.getText();

    try {
      delete = storekeeper.getConnection().setSavepoint("delete");
    } catch (SQLException e) {
      e.printStackTrace();
    }

    if(checkFormat(code)==-1 || checkFormat(amount)==-1 || !storekeeper.searchForProductFromCode(Integer.parseInt(code))) {
      
      lError.setVisible(true);
      return;
    }

    int amountInt;
    int codeInt;
    amountInt = checkFormat(amount);
    codeInt = Integer.parseInt(code);

    if(!storekeeper.isTranactionStarted()){
      storekeeper.setTranactionStarted(true);

      try {
        storekeeper.getConnection().setAutoCommit(false);
        storekeeper.createDelivery();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }

    String name = storekeeper.getProductName(codeInt);

    ProductForDeliver product = new ProductForDeliver(name, code, amountInt);
    storekeeper.addDeliveryProduct(product);

    storekeeper.existingProductDeliver(Integer.parseInt(code),Integer.parseInt(amount),deliverer);

    lSuccess.setVisible(true);

    addToList(storekeeper.getDeliveredProducts());
  }

  public void addToList(ArrayList<ProductForDeliver> deliveredProducts){
    tvProducts.getItems().clear();
    for(ProductForDeliver productForDeliver : deliveredProducts){
      tvProducts.getItems().add(productForDeliver);
    }
  }

  public void bNewProductClick(ActionEvent event) {
    setNewProductPane();
    try {
      delete = storekeeper.getConnection().setSavepoint("delete");
    } catch (SQLException e) {
      e.printStackTrace();
    }
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
    newProductController.setStorekeeper(storekeeper);
    newProductController.setController(controller);
    newProductController.setLoginController(loginController);
    controller.setPane(newProductPane);
  }

  private int checkFormat(String check) {
    int i;
    try {
      i = Integer.parseInt(check);
    } catch (NumberFormatException e) {
      return -1;
    }
    return i;
  }

  public boolean logoutConfirmation() {
    Optional<ButtonType> result = DialogUtils.confirmationDialog("Logout", "Are you sure?");
    if (result.get() == ButtonType.OK) {
      return true;
    }
    return false;
  }

  public void bDeleteClick(ActionEvent event) {
    lError.setVisible(false);
    lSuccess.setVisible(false);
    try {
      storekeeper.getConnection().rollback(delete);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    Object selectedItem = tvProducts.getSelectionModel().getSelectedItem();
    tvProducts.getItems().remove(selectedItem);
  }

  public void bFinishClick(ActionEvent event) {
    lError.setVisible(false);
    lSuccess.setVisible(false);

    storekeeper.setTranactionStarted(false);
    try {
      storekeeper.endDelivery();
      storekeeper.getConnection().commit();
      storekeeper.getConnection().setAutoCommit(true);

    } catch (SQLException e) {
      e.printStackTrace();
    }

    tvProducts.getItems().clear();

    setDisabledPane();
  }

  private void setDisabledPane() {

    tfDeliverer.setDisable(true);
    tfAmount.setDisable(true);
    tfCode.setDisable(true);
    tfAmount.setText("");
    tfDeliverer.setText("");
    tfCode.setText("");
    bAdd.setDisable(true);
    bAddNewProduct.setDisable(true);
    bFinishDelivery.setDisable(true);
    bDelete.setDisable(true);
  }

  public void setStoreKeeper(Storekeeper storeKeeper) {
    this.storekeeper=storeKeeper;
  }
}
