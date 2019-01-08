package controllers;

import elements.Deliver;
import elements.ProductForDeliver;
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
import java.sql.Savepoint;
import java.util.ArrayList;

public class ViewDeliversPaneController {

  public TableView tvDelivers;
  public TableColumn tvcId;
  public TableColumn tvcData;
  public TableColumn tvcDeliverer;
  public TableColumn tvcStorekeeper;
  public TableView tvProducts;
  public TableColumn tvcProduct;
  public TableColumn tvcCode;
  public TableColumn tvcAmount;
  private MainController controller;
  private LoginPaneController loginController;
  private ManagerPaneController managerController;

  Manager manager;


  @FXML
  public void initialize() {
    Application.setUserAgentStylesheet(Application.STYLESHEET_CASPIAN);
    tvcId.setCellValueFactory(new PropertyValueFactory<>("id"));
    tvcData.setCellValueFactory(new PropertyValueFactory<>("date"));
    tvcDeliverer.setCellValueFactory(new PropertyValueFactory<>("deliverer"));
    tvcStorekeeper.setCellValueFactory(new PropertyValueFactory<>("storekeeper"));
    tvcProduct.setCellValueFactory(new PropertyValueFactory<>("name"));
    tvcCode.setCellValueFactory(new PropertyValueFactory<>("code"));
    tvcAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
  }


  void addDeliveriesToList() {
    for(Deliver deliver : manager.getDeliveries()) {
      tvDelivers.getItems().add(deliver);
    }
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

  public void bRefreshClick(ActionEvent event) {
    tvDelivers.getItems().clear();
    manager.downloadDeliveries();
    addDeliveriesToList();
  }

  public void bDeleteClick(ActionEvent event){

    if(tvDelivers.getSelectionModel().getSelectedItem() == null) {
      return;
    }

    Deliver selectedItem = (Deliver) tvDelivers.getSelectionModel().getSelectedItem();
    if(selectedItem == null) {
      return;
    }
    tvDelivers.getItems().remove(selectedItem);
    tvProducts.getItems().clear();
    manager.deleteDelivery(selectedItem.getId());
    manager.downloadDeliveries();
  }

  public void bShowClick(ActionEvent event) {
    tvProducts.getItems().clear();

    Deliver selectedItem = (Deliver) tvDelivers.getSelectionModel().getSelectedItem();
    if(selectedItem == null) {
      return;
    }
    ArrayList<ProductForDeliver> elements = selectedItem.getElements();
    for(ProductForDeliver element : elements) {
      tvProducts.getItems().add(element);
    }
  }

  public void bAddClick(ActionEvent event){
   setAddDeliveryPane("",null);
  }

  void setAddDeliveryPane(String deliverer, Savepoint deleted) {
    FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxmlFiles/AddDeliveryPane.fxml"));
    AnchorPane addDeliveryPane = null;
    try {
      addDeliveryPane = loader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }
    AddDeliveryPaneController addDeliveryController = loader.getController();
    addDeliveryController.setController(controller);
    addDeliveryController.setLoginController(loginController);
    addDeliveryController.setManagerController(managerController);
    addDeliveryController.setViewDeliversPaneController(this);
    addDeliveryController.setDeliverer(deliverer);
    addDeliveryController.setManager(manager);
    addDeliveryController.loadActiveDelivery(manager.getDeliveredProducts());
    addDeliveryController.setDeleted(deleted);
    if(manager.getDeliveredProducts().size() == 0){
      addDeliveryController.disableButtons(true);
    } else {
      addDeliveryController.disableButtons(false);
    }
    controller.setPane(addDeliveryPane);

  }

  public void setManager(Manager manager) {
    this.manager = manager;
  }
}
