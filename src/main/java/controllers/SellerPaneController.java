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
import workers.Seller;

import java.io.IOException;
import java.util.ArrayList;

//TODO dodać delete żeby można było usunąć z rachunku
//TODO dodać przycisk finalizujący transakcje który dodaje do tabeli sale

//TODO gdzies jeszcze trzeba sprawdzać po kliknieciu add czy jest dostępne taka ilosc produktu

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

  private MainController controller;

  private LoginPaneController loginController;

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
    controller.setLoginPane();
  }

  public void bAddClick(ActionEvent event) { //TODO tutaj po kliknięciu trzeba sprawdzić czy dany kod jest w bazie i jak jest to odczytać jaki produkt ma dany kod
    lWarning.setText("");
    //pobieramy kod z taProduct i szukamy go w bazie
    String sCode = taProduct.getText();
    String quantity = taQuantity.getText();

    if(sCode.equals("") || quantity.equals("")) {
      lWarning.setText("Wrong input!");
      return;
    }

    int intQuantity;
    try {
      intQuantity = Integer.parseInt(quantity);
    } catch (NumberFormatException e) {
      lWarning.setText("Wrong input!");
      return;
    }
    int iCode = Integer.parseInt(sCode);

    if(!seller.searchForProductFromCode(iCode)){
      lWarning.setText("No such product!");
      return;
    }else if(seller.checkAmount(iCode) > intQuantity){
      BillElement billElement = new BillElement(seller.getProductName(iCode),intQuantity, seller.getPrice(iCode));
      seller.createSale(iCode,intQuantity);
      tvBill.getItems().add(billElement);
      setTotal();
    } else {
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

    //TODO pobrac wszystko z tvBill i dodac do tabeli sale
    //tvBill.getItems();

    //TODO pobrac całą cene total (UWAGA bo tam jest string z dodatkiem zł na końcu!)
    //lTotal.getText();

    tvBill.getItems().clear();
    lTotal.setText("0,00 zł");
  }

  public void bDeleteClick(ActionEvent event) {
    Object selectedItem = tvBill.getSelectionModel().getSelectedItem();
    tvBill.getItems().remove(selectedItem);
    setTotal();
  }

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
