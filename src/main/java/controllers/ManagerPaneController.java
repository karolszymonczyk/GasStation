package controllers;

import elements.ManagerBill;
import elements.ManagerSale;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
//import utils.DialogUtils;
import workers.Manager;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

//TODO tutaj i w sellerze nwm czy wgl potrzebne sa te wszystkie tvc i nie wystarcza tylko tabele tv

//TODO na odwrót niż w bazie jest bill i sale XD

//TODO zmienić amount da double wszędzie bo nie można spredawać benzynki

//TODO dodac jeszcze dodawanie i usuwanie sprzedaży

//
public class ManagerPaneController {

  public TableView tvSales;
  public TableColumn tvcId;
  public TableColumn tvcData;
  public TableColumn tvcSeller;
  public TableColumn tvcValue;
  public TableView tvBill;
  public TableColumn tvcProduct;
  public TableColumn tvcAmount;
  public TableColumn tvcPrice;

  private MainController controller;
  private LoginPaneController loginController;
  private Manager manager;
  private ArrayList<ManagerSale> bills;

  @FXML
  public void initialize() {
    Application.setUserAgentStylesheet(Application.STYLESHEET_CASPIAN);
    tvcId.setCellValueFactory(new PropertyValueFactory<>("id"));
    tvcData.setCellValueFactory(new PropertyValueFactory<>("date"));
    tvcSeller.setCellValueFactory(new PropertyValueFactory<>("seller"));
    tvcValue.setCellValueFactory(new PropertyValueFactory<>("value"));
    tvcProduct.setCellValueFactory(new PropertyValueFactory<>("product"));
    tvcAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
    tvcPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
//    addToSaleList();
  }

  public void addToSaleList() {

    bills = manager.getBills();

    for (ManagerSale managerSale : bills) {
      addSale(managerSale);
//      for (ManagerBill managerBill : managerSale.getElements()) {
//        System.out.println("element = " + managerBill);
//      }
    }
  }

  public void addSale(ManagerSale managerSale) {
    tvSales.getItems().add(managerSale);
  }

  public void setController(MainController controller) {
    this.controller = controller;
  }

  public void setLoginController(LoginPaneController LoginController) {
    this.loginController = LoginController;
  }

  public void bLogoutClick(ActionEvent event) {
    if (true) {
      controller.setLoginPane();
    }
  }

  public void bShowClick(ActionEvent event) {
    tvBill.getItems().clear();

    ManagerSale selectedItem = (ManagerSale) tvSales.getSelectionModel().getSelectedItem();
    if (selectedItem == null) {
      return;
    }
    ArrayList<ManagerBill> elements = selectedItem.getElements();
    for (ManagerBill element : elements) {
      tvBill.getItems().add(element);
    }
  }

  public void bWorkersClick(ActionEvent event) {
    setViewWorkersPane();
  }

  public void setViewWorkersPane() {
    FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxmlFiles/ViewWorkersPane.fxml"));
    AnchorPane viewWorkersPane = null;
    try {
      viewWorkersPane = loader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }
    ViewWorkersPaneController viewWorkersController = loader.getController();
    viewWorkersController.setController(controller);
    viewWorkersController.setLoginController(loginController);
    viewWorkersController.setManagerController(this);
    viewWorkersController.setManager(manager);
    viewWorkersController.addWorkerList();
    controller.setPane(viewWorkersPane);
  }

  public void bDeliversClick(ActionEvent event) {
    setViewDeliveryPane();
  }

  public void setViewDeliveryPane() {
    FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxmlFiles/ViewDeliversPane.fxml"));
    AnchorPane viewDeliversPane = null;
    try {
      viewDeliversPane = loader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }
    ViewDeliversPaneController viewDeliversController = loader.getController();
    viewDeliversController.setController(controller);
    viewDeliversController.setLoginController(loginController);
    viewDeliversController.setManager(manager);
    viewDeliversController.addDeliveriesToList();
    controller.setPane(viewDeliversPane);
  }

  public void bProductsClick(ActionEvent event) {
    setViewProductsPane();
  }

  public void setViewProductsPane() {
    FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxmlFiles/ViewProductsPane.fxml"));
    AnchorPane viewProductsPane = null;
    try {
      viewProductsPane = loader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }
    ViewProductsPaneController viewProductsController = loader.getController();
    viewProductsController.setController(controller);
    viewProductsController.setLoginController(loginController);
    viewProductsController.setManagerController(this);
    viewProductsController.setManager(manager);
    viewProductsController.addProductList();
    controller.setPane(viewProductsPane);
  }

  public void bRefreshClick(ActionEvent event) {
    manager.downloadAll();
  }

  public void bDeleteSaleClick(ActionEvent event) { //TODO można dodać ze jak nie ma selected item to nic nie robi
    ManagerSale selectedItem = (ManagerSale) tvSales.getSelectionModel().getSelectedItem();
    tvSales.getItems().remove(selectedItem);
    manager.deleteSale(selectedItem.getId());
    tvBill.getItems().clear();
  }

  public void bAddSaleClick(ActionEvent event) {
    setAddPane();
  }

  private void setAddPane() {
    FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxmlFiles/AddSalePane.fxml"));
    AnchorPane addSalePane = null;
    try {
      addSalePane = loader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }
    AddSalePaneController addSaleController = loader.getController();
    addSaleController.setController(controller);
    addSaleController.setLoginController(loginController);
    addSaleController.setManager(manager);
    addSaleController.addToProductList();
    controller.setPane(addSalePane);
  }

  public void setManager(Manager manager) {
    this.manager=manager;
  }

  public Manager getManager() {
    return manager;
  }

  //  public boolean logoutConfirmation() {
//    Optional<ButtonType> result = DialogUtils.confirmationDialog("Logout", "Are you sure?");
//    if (result.get() == ButtonType.OK) {
//      return true;
//    }
//    return false;
//  }
//    public void setManager (Manager manager){
//      this.manager = manager;
//    }


  }

