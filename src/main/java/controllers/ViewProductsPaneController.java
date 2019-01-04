package controllers;

import elements.Product;
import elements.ProductView;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class ViewProductsPaneController {

  public TableView tvProducts;
  public TableColumn tvcCode;
  public TableColumn tvcName;
  public TableColumn tvcPrice;
  public TableColumn tvcTax;
  public TableColumn tvcAmount;

  private MainController controller;
  private LoginPaneController loginController;
  private ManagerPaneController managerController;

  @FXML
  public void initialize() {
    Application.setUserAgentStylesheet(Application.STYLESHEET_CASPIAN);
    tvcCode.setCellValueFactory(new PropertyValueFactory<>("code"));
    tvcName.setCellValueFactory(new PropertyValueFactory<>("name"));
    tvcPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    tvcTax.setCellValueFactory(new PropertyValueFactory<>("tax"));
    tvcAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
    addToProductsList();
  }

  private void addToProductsList() {
    addProduct("161", "Placuszki", 30.99, 0.1, 40);
  }

  private void addProduct(String code, String name, double price, double tax, int amount) {

    ProductView productView = new ProductView(code, name, price, tax, amount);
    tvProducts.getItems().add(productView);
  }

  public void setController(MainController controller) {
    this.controller = controller;
  }

  public void setLoginController(LoginPaneController LoginController) {
    this.loginController = LoginController;
  }

  public void setManagerController(ManagerPaneController managerController) {
    this.managerController = managerController;
  }

  public void bBackClick(ActionEvent event) {
    loginController.setManagerPane();
  }

  public void bAddClick(ActionEvent event) {
    FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxmlFiles/AddProductPane.fxml"));
    AnchorPane addProductPane = null;
    try {
      addProductPane = loader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }

    AddProductPaneController addProductController = loader.getController();
    addProductController.setController(controller);
    addProductController.setManagerController(managerController);
    controller.setPane(addProductPane);
  }

  public void bDeleteClick(ActionEvent event) {
    Object selectedItem = tvProducts.getSelectionModel().getSelectedItem();
    tvProducts.getItems().remove(selectedItem);
  }
}
