package controllers;

//import dbConnection.Backup;
import dbConnection.Backup;
import elements.BillElement;
import elements.Customer;
import elements.ManagerBill;
import elements.ManagerSale;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import workers.Manager;
import workers.Seller;

import java.io.IOException;
import java.util.ArrayList;

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
  public ChoiceBox cbPeriod;

  private MainController controller;
  private LoginPaneController loginController;
  private Manager manager;
  private ArrayList<ManagerSale> bills;

  String username;
  String password;

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
    cbPeriod.setValue("this month");
    cbPeriod.getItems().addAll("this month", "two months", "this year", "all");
    //TODO tutaj se poustawiaj a domyślnie daj z tego miesiąca żeby się wczytywały
    cbPeriod.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
      @Override
      public void changed(ObservableValue observable, Object oldValue, Object newValue) {
        switch (newValue.toString()) {
          case "this month":
            System.out.println("WCZYTUJE Z TEGO MSC");
            break;
          case "two months":
            System.out.println("WCZYTUJE Z 2 MSC");
            break;
          case "this year":
            System.out.println("WCZYTUJE Z TEGO ROKU");
            break;
          case "all":
            System.out.println("WCZYTUJE WSZYSTKO");
            break;
        }
      }
    });
  }

  void addToSaleList() {

    bills = manager.getBills();

    for (ManagerSale managerSale : bills) {
      addSale(managerSale);
    }
  }

  private void addSale(ManagerSale managerSale) {
    tvSales.getItems().add(managerSale);
  }

  public void setController(MainController controller) {
    this.controller = controller;
  }

  public void setLoginController(LoginPaneController LoginController) {
    this.loginController = LoginController;
  }

  public void bLogoutClick(ActionEvent event) {
    controller.setLoginPane();
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

  void setViewWorkersPane() {
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
    setViewDeliversPane();
  }

  void setViewDeliversPane() {

    manager.downloadDeliveries();

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
    viewDeliversController.setManagerController(this);
    viewDeliversController.setManager(manager);
    viewDeliversController.addDeliveriesToList();
    controller.setPane(viewDeliversPane);
  }

  public void bProductsClick(ActionEvent event) {
    setViewProductsPane();
  }

  void setViewProductsPane() {
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

  public void bDeleteSaleClick(ActionEvent event) {

    if (tvSales.getSelectionModel().getSelectedItem() == null) {
      return;
    }
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
    if (manager.getActiveBill().size() == 0) {
      addSaleController.disableButtons(true);
    } else {
      addSaleController.disableButtons(false);
    }
    controller.setPane(addSalePane);
  }

  public void setManager(Manager manager) {
    this.manager = manager;
  }

  public Manager getManager() {
    return manager;
  }

  public void bViewCustomersClick(ActionEvent event) {
    setViewCustomersPane();
  }

  public void setViewCustomersPane() {

    manager.downloadCustomers();

    FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxmlFiles/ViewCustomersPane.fxml"));
    AnchorPane viewCustomersPane = null;
    try {
      viewCustomersPane = loader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }
    ViewCustomersPaneController viewCustomersController = loader.getController();
    viewCustomersController.setController(controller);
    viewCustomersController.setLoginController(loginController);
    viewCustomersController.setManager(manager);
    viewCustomersController.setManagerController(this);
    viewCustomersController.addCustomersList();
    controller.setPane(viewCustomersPane);
  }

  public void bBackupClick(ActionEvent event) { //TODO dodać Backup ZMIENIC NULLA!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    Backup backup = new Backup();
    backup.executeBackUp();

  }



  public void setUsername(String username) {
    this.username = username;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}

