package controllers;

import elements.BillElement;
import elements.Product;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
//
public class AddSalePaneController {

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

  @FXML
  public void initialize() {
    Application.setUserAgentStylesheet(Application.STYLESHEET_CASPIAN);
    tvName.setCellValueFactory(new PropertyValueFactory<>("Name"));
    tvCode.setCellValueFactory(new PropertyValueFactory<>("Code"));
    tvcProduct.setCellValueFactory(new PropertyValueFactory<>("Product"));
    tvcPrice.setCellValueFactory(new PropertyValueFactory<>("Price"));
    tvcQuantity.setCellValueFactory(new PropertyValueFactory<>("Quantity"));
    tvcSum.setCellValueFactory(new PropertyValueFactory<>("Sum"));
    addToProductList();
  }

  public void addToProductList() { //TODO tutaj trzeba podpiąc produkty z bazy
    addProduct("Narty", "192342144");
    addProduct("Papieroski", "212342144");
    addProduct("Kajzerki", "312342144");
    addProduct("Piwo", "412342144");
    addProduct("Snickers", "512342144");
    addProduct("Bounty", "612342144");
    addProduct("Haribo", "712342144");
    addProduct("Benzyna", "812342144");
    addProduct("Gumy kulki", "912342144");
    addProduct("Prince Polo", "102342144");
    addProduct("Gaz", "112342144");
    addProduct("Popcorn", "123542134");
  }

  public void addProduct(String name, String code) {
    Product product = new Product(name, code);
    tvProducts.getItems().add(product);
  }

  public void setController(MainController controller) {
    this.controller = controller;
  }

  public void setLoginController(LoginPaneController LoginController) {
    this.loginController = LoginController;
  }

  public void bAddClick(ActionEvent event) { //TODO tutaj po kliknięciu trzeba sprawdzić czy dany kod jest w bazie i jak jest to odczytać jaki produkt ma dany kod
    lWarning.setText("");
    //pobieramy kod z taProduct i szukamy go w bazie
    String code = taProduct.getText();
    String quantity = taQuantity.getText();

    if(code.equals("") || quantity.equals("")) {
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

    //TODO tutaj dodać że jak nie ma w bazie takiego kodu to tak jak wyżej ustawić label i return
    //TODO trzeba też tutaj sprawdzić czy jest tyle dostępne

    //TODO jak kod jest w bazie to trzeba pobrać jego nazwe i ilosc

    //TODO tutaj zamiast code trzeba podać nazwę produktu o takim kodzie i jego cene
    //BillElement billElement = new BillElement(code, intQuantity, 12.99);

    //tvBill.getItems().add(billElement);
    setTotal();

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

    if(tfCustomer.getText().equals("")) {
      System.out.println("NULL");
    } else {
      System.out.println(tfCustomer.getText());
    }

    //TODO pobrac wszystko z tvBill i dodac do tabeli sale
    //tvBill.getItems();

    //TODO pobrac całą cene total (UWAGA bo tam jest string z dodatkiem zł na końcu!)
    //lTotal.getText();

    tvBill.getItems().clear();
    lTotal.setText("0,00 zł");
    tfCustomer.setText("");
  }

  public void bDeleteClick(ActionEvent event) {
    Object selectedItem = tvBill.getSelectionModel().getSelectedItem();
    tvBill.getItems().remove(selectedItem);
  }

  public void bBackClick(ActionEvent event) {
    loginController.setManagerPane();
  }
}
