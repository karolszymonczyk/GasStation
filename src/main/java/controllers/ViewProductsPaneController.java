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
import workers.Manager;

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

  Manager manager;

  @FXML
  public void initialize() {
    Application.setUserAgentStylesheet(Application.STYLESHEET_CASPIAN);
    tvcCode.setCellValueFactory(new PropertyValueFactory<>("code"));
    tvcName.setCellValueFactory(new PropertyValueFactory<>("name"));
    tvcPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    tvcTax.setCellValueFactory(new PropertyValueFactory<>("tax"));
    tvcAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
  }

  void addProductList() {

    for(ProductView productView : manager.getProducts())
    tvProducts.getItems().add(productView);
  }

  public void setController(MainController controller) {
    this.controller = controller;
  }

  public void setLoginController(LoginPaneController LoginController) {
    this.loginController = LoginController;
  }

  void setManagerController(ManagerPaneController managerController) {
    this.managerController = managerController;
  }

  public void bBackClick(ActionEvent event) {
    loginController.setManagerPane();
  }

  public void bAddClick(ActionEvent event) {
    FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxmlFiles/AddProductPane.fxml"));
    AnchorPane addWorkerPane = null;
    try {
      addWorkerPane = loader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }

    AddProductPaneController addProductPaneController = loader.getController();
    addProductPaneController.setController(controller);
    addProductPaneController.setManagerController(managerController);
    addProductPaneController.setManager(manager);
    controller.setPane(addWorkerPane);
  }

  public void bDeleteClick(ActionEvent event) {

    if(tvProducts.getSelectionModel().getSelectedItem() == null) {
      return;
    }

    ProductView selectedItem = (ProductView) tvProducts.getSelectionModel().getSelectedItem();
    manager.deleteProduct(Integer.parseInt(selectedItem.getCode()));
    tvProducts.getItems().remove(selectedItem);
    manager.getProducts().remove(selectedItem);
  }

  public void setManager(Manager manager) {
    this.manager = manager;
  }

  public void bRefreshClick(ActionEvent event) {
    manager.downloadProducts();

    tvProducts.getItems().clear();

    addProductList();
  }
}
