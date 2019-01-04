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

import utils.DialogUtils;

import java.io.IOException;
import java.sql.Savepoint;
import java.util.Optional;
import workers.Seller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

//TODO dodać delete żeby można było usunąć z rachunku
//TODO dodać przycisk finalizujący transakcje który dodaje do tabeli sale

//TODO gdzies jeszcze trzeba sprawdzać po kliknieciu add czy jest dostępne taka ilosc produktu

//TODO zrobic dodawanie karty i uwzględnienie zniżki

public class SellerPaneController {

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

  private MainController controller;

  private LoginPaneController loginController;

  Savepoint delete;

  private Seller seller;

  @FXML
  public void initialize() {
    Application.setUserAgentStylesheet(Application.STYLESHEET_CASPIAN);
    tvName.setCellValueFactory(new PropertyValueFactory<>("Name"));
    tvCode.setCellValueFactory(new PropertyValueFactory<>("Code"));
    tvcProduct.setCellValueFactory(new PropertyValueFactory<>("Product"));
    tvcPrice.setCellValueFactory(new PropertyValueFactory<>("Price"));
    tvcQuantity.setCellValueFactory(new PropertyValueFactory<>("Quantity"));
    tvcSum.setCellValueFactory(new PropertyValueFactory<>("Sum"));

  }

  void addToProductList(ArrayList<Product> products) {

    for(Product product : products) {
      addProduct(product);
    }
  }

  private void addProduct(Product product) {
    tvProducts.getItems().add(product);
  }

  public void setController(MainController controller) {
    this.controller = controller;
  }

  void setLoginController(LoginPaneController LoginController) {
    this.loginController = LoginController;
  }

  public void bLogoutClick(ActionEvent event) {

    if(logoutConfirmation()) {
      try {
      seller.getConnection().rollback();
      seller.getConnection().setAutoCommit(true);
    } catch (SQLException e) {
      System.out.println("rollback się nie wykonał ponieważ nie było aktywnej tranzakcji.");
    }
    seller.deleteBill();
    controller.setLoginPane();
    }
  }

  public void bAddClick(ActionEvent event) {


    lWarning.setText("");
    String sCode = taProduct.getText();
    String quantity = taQuantity.getText();
    int intQuantity;
    int iCode;


    if(!seller.isTransactionStarted()){
      seller.setTransactionStarted(true);

      try {
        seller.getConnection().setAutoCommit(false);
        seller.createBill();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }

    if(sCode.equals("") || quantity.equals("")) {
      lWarning.setText("Wrong input!");
      return;
    }

    try {
      intQuantity = Integer.parseInt(quantity);
      iCode = Integer.parseInt(sCode);
    } catch (NumberFormatException e) {
      lWarning.setText("Wrong input!");
      return;
    }

    if(!seller.searchForProductFromCode(iCode)){
      lWarning.setText("No such product!");
      return;
    }else if(seller.checkAmount(iCode) >= intQuantity && seller.isTransactionStarted()){
      try {
       delete =  seller.getConnection().setSavepoint("delete");
      } catch (SQLException e) {
        e.printStackTrace();
      }
      float price = seller.getPrice(iCode);
      seller.createSale(iCode,intQuantity);
      seller.addToBill(price*intQuantity);
      BillElement billElement = new BillElement(seller.getProductName(iCode),intQuantity,price,-1);
      tvBill.getItems().add(billElement);
      setTotal();
    }  else {
      lWarning.setText("Not enough products!");
      return;
    }

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


    seller.setTransactionStarted(false);
    Integer NIP;

    if(tfCustomer.getText().equals("")) {
      NIP = null;
      seller.closeBillWithoutCustomer();
    } else {
      NIP = Integer.parseInt(tfCustomer.getText());
      seller.closeBill(NIP);
    }

    try {

      seller.getConnection().commit();
      seller.getConnection().setAutoCommit(true);

    } catch (SQLException e) {
      e.printStackTrace();
    }



    //TODO (DONE)  PO KLIKNIECIU SALE WSZYSTKIE KOMMITY SIĘ WYKONUĄJĄ(TE KTORE  NIE WYKONAŁY SIĘ W METODZIE CREATE SALE (INSERTOWANIE DO TABELI SALE W DB))

    //TODO pobrac wszystko z tvBill i dodac do tabeli sale
    //tvBill.getItems();

    //TODO pobrac całą cene total (UWAGA bo tam jest string z dodatkiem zł na końcu!)
    //lTotal.getText();

    tvBill.getItems().clear();
    lTotal.setText("0,00 zł");
    tfCustomer.setText("");
  }

  public void bDeleteClick(ActionEvent event) {
    BillElement selectedItem = (BillElement) tvBill.getSelectionModel().getSelectedItem();
    try {
      seller.getConnection().rollback(delete);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    tvBill.getItems().remove(selectedItem);
    setTotal();
  }
//
  public void bAvailabilityClick(ActionEvent event) {
    //setAvailabilityPane();
    Product selectedItem = (Product) tvProducts.getSelectionModel().getSelectedItem();
    String sCode = selectedItem.getCode();
    int iCode = Integer.parseInt(sCode);
    int amount = seller.checkAmount(iCode);
    lWarning.setText("Available amount is : " + amount);
  }

  public void bCardClick(ActionEvent event) {
    setCardPane();
  }

  public void setCardPane() {
    FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxmlFiles/CardPane.fxml"));
    AnchorPane cardPane = null;
    try {
      cardPane = loader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }
    CardPaneController cardController = loader.getController();
    cardController.setSeller(seller);
    cardController.setController(controller);
    cardController.setLoginController(loginController);
    controller.setPane(cardPane);
  }

  public boolean logoutConfirmation() {
    Optional<ButtonType> result = DialogUtils.confirmationDialog("Logout", "Are you sure?");
    if (result.get() == ButtonType.OK) {
      return true;
    }
    return false;
  }
  
  public void setAvailabilityPane() {
    FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxmlFiles/AvailabilityPane.fxml"));
    AnchorPane availabilityPane = null;
    try {
      availabilityPane = loader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }
    AvailabilityPaneController availabilityController = loader.getController();
    availabilityController.setController(controller);
    availabilityController.setLoginController(loginController);
    availabilityController.setTvProducts(tvProducts);
    controller.setPane(availabilityPane);
  }

  public void setSeller(Seller seller) {
    this.seller = seller;
  }


}
