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
  private Storekeeper storekeeper;
  boolean transactionStarted = false;

  Savepoint delete;


  MainController controller;
  LoginPaneController loginController;

  String deliverer;

  @FXML
  public void initialize() {
    Application.setUserAgentStylesheet(Application.STYLESHEET_CASPIAN);
    disableButtons(true);
    tvcName.setCellValueFactory(new PropertyValueFactory<>("name"));
    tvcCode.setCellValueFactory(new PropertyValueFactory<>("code"));
    tvcAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
  }

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
        System.out.println("No active transaction - no rollback.");
      }
      storekeeper.deleteDelivery();
      controller.setLoginPane();
    }
  }

  public void bAddClick(ActionEvent event) {

    lError.setVisible(false);
    lSuccess.setVisible(false);

    disableButtons(false);

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

    if(!storekeeper.isTransactionStarted()){
      storekeeper.setTransactionStarted(true);

      try {
        storekeeper.getConnection().setAutoCommit(false);
        System.out.println("Zacząłem tranze");
        storekeeper.createDelivery();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }

    String name = storekeeper.getProductName(codeInt);

    ProductForDeliver product = new ProductForDeliver(name, code, amountInt);
    storekeeper.addDeliveryProduct(product);

    storekeeper.existingProductDeliver(Integer.parseInt(code),Integer.parseInt(amount));

    lSuccess.setVisible(true);

    addToList(storekeeper.getDeliveredProducts());

    tfCode.setText("");
    tfAmount.setText("");
  }

  void addToList(ArrayList<ProductForDeliver> deliveredProducts){
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
    newProductController.setDeliverer(tfDeliverer.getText());
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

  private boolean logoutConfirmation() {
    Optional<ButtonType> result = DialogUtils.confirmationDialog("Logout", "Are you sure?");
    return result.get() == ButtonType.OK;
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
    if(tvProducts.getItems().isEmpty()){
      disableButtons(true);
    }
  }

  public void bFinishClick(ActionEvent event) {

    disableButtons(true);

    lError.setVisible(false);
    lSuccess.setVisible(false);
    storekeeper.setTransactionStarted(false);

    try {

      storekeeper.setDeliverer(tfDeliverer.getText());
      storekeeper.endDelivery();
      System.out.println("USTAWIAM DELIVERERA = " + tfDeliverer.getText());
      storekeeper.getConnection().commit();
      System.out.println("SKOńczyłem tranze");
      storekeeper.getConnection().setAutoCommit(true);

    } catch (SQLException e) {
      e.printStackTrace();
    }
    storekeeper.getDeliveredProducts().clear();
    tvProducts.getItems().clear();
    clearTextFields();
  }

  public void setDeliverer(String deliverer) {
    tfDeliverer.setText(deliverer);
  }

  void setStoreKeeper(Storekeeper storeKeeper) {
    this.storekeeper=storeKeeper;
  }

  void disableButtons(boolean bool){
    bFinishDelivery.setDisable(bool);
    bDelete.setDisable(bool);
  }

  private void clearTextFields(){
    tfDeliverer.setText("");
    tfCode.setText("");
    tfAmount.setText("");
  }
}
