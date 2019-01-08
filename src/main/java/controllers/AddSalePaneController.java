package controllers;

import elements.BillElement;
import elements.Product;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import utils.ErrorUtils;
import workers.Manager;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Savepoint;

public class AddSalePaneController implements ErrorUtils{

  @FXML
  public TableView tvProducts;
  public TableColumn tvName;
  public TableColumn tvCode;
  public TableColumn tvcProduct;
  public TableColumn tvcPrice;
  public TableColumn tvcQuantity;
  public TableColumn tvcSum;
  public TextField taProduct;
  public TextField taQuantity;
  public Label lTotal;
  public Label lWarning;
  public TableView tvBill;
  public TextField tfCustomer;

  public Button bSell;
  public Button bDelete;

  private MainController controller;

  private LoginPaneController loginController;
  private Manager manager;

  Savepoint delete;

  @FXML
  public void initialize() {
    Application.setUserAgentStylesheet(Application.STYLESHEET_CASPIAN);
    disableButtons(true);
    tvName.setCellValueFactory(new PropertyValueFactory<>("Name"));
    tvCode.setCellValueFactory(new PropertyValueFactory<>("Code"));
    tvcProduct.setCellValueFactory(new PropertyValueFactory<>("Product"));
    tvcPrice.setCellValueFactory(new PropertyValueFactory<>("Price"));
    tvcQuantity.setCellValueFactory(new PropertyValueFactory<>("Quantity"));
    tvcSum.setCellValueFactory(new PropertyValueFactory<>("Sum"));
  }


  public void addToProductList() {
    for(Product product : manager.getProductsToSale())
    tvProducts.getItems().add(product);
  }

  public void setController(MainController controller) {
    this.controller = controller;
  }

  public void setLoginController(LoginPaneController LoginController) {
    this.loginController = LoginController;
  }

  public void bAddClick(ActionEvent event) {

    lWarning.setText("");

    String sCode = taProduct.getText();
    String quantity = taQuantity.getText();

    int intQuantity;
    int iCode;

    if(!manager.isTransactionStarted()){
      manager.setTransactionStarted(true);

      try {
        manager.getConnection().setAutoCommit(false);
        manager.createBill();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }


    if(sCode.equals("") || quantity.equals("")) {
      lWarning.setText("Empty field!");
      return;
    }

    if(!ErrorUtils.checkInt(sCode)) {
      lWarning.setText("Wrong input!");
      return;
    } else if (!ErrorUtils.checkInt(quantity)) {
      lWarning.setText("Wrong input!");
      return;
    }

    intQuantity = Integer.parseInt(quantity);
    iCode = Integer.parseInt(sCode);

    if(!manager.searchForProductFromCode(iCode)){
      lWarning.setText("No such product!");
      return;
    }else if(manager.checkAmount(iCode) >= intQuantity && manager.isTransactionStarted()){
      try {
        delete =  manager.getConnection().setSavepoint("delete");
      } catch (SQLException e) {
        e.printStackTrace();
      }
      double price = manager.getPrice(iCode);
      manager.createSale(iCode,intQuantity);
      manager.addToBill(price*intQuantity);
      BillElement billElement = new BillElement(manager.getProductName(iCode),intQuantity,price);
      tvBill.getItems().add(billElement);
      setTotal();
    }  else {
      lWarning.setText("Not enough products!");
      return;
    }

//    setTotal();

    disableButtons(false);

    taProduct.setText("");
    taQuantity.setText("");
  }

  public void setTotal() {

    double total = 0;
    BillElement billElement = new BillElement();

    for(int i = 0; i < tvBill.getItems().size(); i ++) {
      billElement = (BillElement)tvBill.getItems().get(i);
      total += billElement.getSum();
    }
    double totalRound = Math.round(total*100.0)/100.0;

    lTotal.setText(Double.toString(totalRound) + " zł");
  }

  public void bSellClick(ActionEvent event) {

    disableButtons(true);

    manager.setTransactionStarted(false);
    Integer NIP;

    if(tfCustomer.getText().equals("")) {
      NIP = null;
      manager.closeBillWithoutCustomer();
    }

    if(!ErrorUtils.checkInt(tfCustomer.getText())) {
      lWarning.setText("Wrong NIP format!");
      return;
    }

    NIP = Integer.parseInt(tfCustomer.getText());
    manager.closeBill(NIP);

    try {

      manager.getConnection().commit();
      manager.getConnection().setAutoCommit(true);

    } catch (SQLException e) {
      e.printStackTrace();
    }

    tvBill.getItems().clear();
    lTotal.setText("0,00 zł");
    lWarning.setText("");
    clearTextFields();
  }

  public void bDeleteClick(ActionEvent event) {

    BillElement selectedItem = (BillElement) tvBill.getSelectionModel().getSelectedItem();
    try {
      manager.getConnection().rollback(delete);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    tvBill.getItems().remove(selectedItem);
    setTotal();

    if(tvBill.getItems().isEmpty()){
      disableButtons(true);
    }
  }

  public void bBackClick(ActionEvent event) {


      try {
        manager.getConnection().rollback();
        manager.getConnection().setAutoCommit(true);
      } catch (SQLException e) {
        System.out.println("rollback się nie wykonał ponieważ nie było aktywnej tranzakcji.");
      }
      manager.deleteBill();
      manager.downloadBills();
      loginController.setManagerPane();
  }

  public void setManager(Manager manager) {
    this.manager=manager;
  }

  public void bAvailabilityClick(ActionEvent event) {

    Product selectedItem = (Product) tvProducts.getSelectionModel().getSelectedItem();
    String sCode = selectedItem.getCode();
    int iCode = Integer.parseInt(sCode);
    int amount = manager.checkAmount(iCode);
    lWarning.setText("Available amount is : " + amount);
  }

  public void disableButtons(boolean bool){
    bSell.setDisable(bool);
    bDelete.setDisable(bool);
  }

  public void clearTextFields(){
    tfCustomer.setText("");
    taQuantity.setText("");
    taProduct.setText("");
  }
}
