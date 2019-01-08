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
import utils.ErrorUtils;

import java.io.IOException;
import java.sql.Savepoint;
import java.util.Optional;

import utils.ErrorUtils;
import workers.Seller;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.Optional;

//TODO zrobic dodawanie karty i uwzględnienie zniżki
public class SellerPaneController implements ErrorUtils {

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

  private Savepoint delete;

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

    for (Product product : products) {
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

    if (logoutConfirmation()) {
      try {
        seller.getConnection().rollback();
        seller.getConnection().setAutoCommit(true);
      } catch (SQLException e) {
        System.out.println("No active transaction - no rollback.");
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


    if (!seller.isTransactionStarted()) {
      seller.setTransactionStarted(true);

      try {
        seller.getConnection().setAutoCommit(false);
        seller.createBill();
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

    if(!seller.searchForProductFromCode(iCode)){
      lWarning.setText("No such product!");
      return;
    } else if (seller.checkAmount(iCode) >= intQuantity && seller.isTransactionStarted()) {
      try {
        delete = seller.getConnection().setSavepoint("delete");
      } catch (SQLException e) {
        e.printStackTrace();
      }
      double price = seller.getPrice(iCode);
      seller.createSale(iCode, intQuantity);
      seller.addToBill(price * intQuantity);
      BillElement billElement = new BillElement(seller.getProductName(iCode), intQuantity, price);
      updateActiveBill(billElement);
      setTotal();
    } else {
      lWarning.setText("Not enough products!");
      return;
    }

    disableButtons(false);

    taProduct.setText("");
    taQuantity.setText("");
  }

  private void updateActiveBill(BillElement billElement) {
    seller.addToActiveBill(billElement);
    tvBill.getItems().clear();
    for (BillElement be : seller.getActiveBill()) {
      tvBill.getItems().add(be);
    }
  }

  private void setTotal() {
    double total = 0;
    BillElement billElement;

    for (int i = 0; i < tvBill.getItems().size(); i++) {
      billElement = (BillElement) tvBill.getItems().get(i);
      total += billElement.getSum();
    }
    double totalRound = Math.round(total * 100.0) / 100.0;

    lTotal.setText(Double.toString(totalRound) + " zł");
  }

  public void bSellClick(ActionEvent event) {

    Integer NIP;

    if (tfCustomer.getText().equals("")) {
      seller.closeBillWithoutCustomer();
    } else if(!ErrorUtils.checkInt(tfCustomer.getText())) {
      lWarning.setText("Wrong NIP format!");
      return;
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
    disableButtons(true);

    seller.setTransactionStarted(false);

    seller.getActiveBill().clear();

    tvBill.getItems().clear();
    lTotal.setText("0,00 zł");
    lWarning.setText("");
    clearTextFields();
  }

  public void bDeleteClick(ActionEvent event) {
    BillElement selectedItem = (BillElement) tvBill.getSelectionModel().getSelectedItem();
    try {
      seller.getConnection().rollback(delete);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    seller.getActiveBill().remove(selectedItem);
    tvBill.getItems().remove(selectedItem);
    setTotal();

    if (tvBill.getItems().isEmpty()) {
      disableButtons(true);
    }
  }

  public void bAvailabilityClick(ActionEvent event) {
    Product selectedItem = (Product) tvProducts.getSelectionModel().getSelectedItem();
    String sCode = selectedItem.getCode();
    int iCode = Integer.parseInt(sCode);
    int amount = seller.checkAmount(iCode);
    lWarning.setText("Available amount is : " + amount);
  }

  public void bCardClick(ActionEvent event) {
    setCardPane();
  }

  private void setCardPane() {
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
    cardController.setSellerController(this);
    cardController.setCustomer(tfCustomer.getText());
    controller.setPane(cardPane);
  }

  private boolean logoutConfirmation() {
    Optional<ButtonType> result = DialogUtils.confirmationDialog("Logout", "Are you sure?");
    return result.get() == ButtonType.OK;
  }

  public void setSeller(Seller seller) {
    this.seller = seller;
  }


  void setCustomer(String customer) {
    tfCustomer.setText(customer);
  }

  void disableButtons(boolean bool) {
    bSell.setDisable(bool);
    bDelete.setDisable(bool);
  }

  private void clearTextFields() {
    tfCustomer.setText("");
    taQuantity.setText("");
    taProduct.setText("");
  }

  void updateBillList(ArrayList<BillElement> billElements) {
    for (BillElement billElement : billElements) {
      tvBill.getItems().add(billElement);
    }
  }
}
