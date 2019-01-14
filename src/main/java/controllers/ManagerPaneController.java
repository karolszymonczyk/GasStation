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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import utils.DialogUtils;
import workers.Manager;
import workers.Seller;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

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
  public Label lError;
  public Label lSuccessB;
  public Label lSuccessR;

  private MainController controller;
  private LoginPaneController loginController;
  private Manager manager;
  private ArrayList<ManagerSale> bills;

  private String username;
  private String password;

  int billChoice = 5;

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
    cbPeriod.setValue("this day");
    cbPeriod.getItems().addAll("this day", "this month", "two months", "this year", "all");
    cbPeriod.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
      @Override
      public void changed(ObservableValue observable, Object oldValue, Object newValue) {
        switch (newValue.toString()) {
          case "this month":
            manager.downloadBills(1);
            billChoice =1;
            addToSaleList();
            break;
          case "two months":
            manager.downloadBills(2);
            billChoice=2;
            addToSaleList();
            break;
          case "this year":
            manager.downloadBills(3);
            billChoice=3;
            addToSaleList();
            break;
          case "all":
            manager.downloadBills(4);
            billChoice=4;
            addToSaleList();
            break;
          case "this day":
            manager.downloadBills(5);
            billChoice=5;
            System.out.println("5");
            addToSaleList();
            break;
        }
      }
    });
  }

  void addToSaleList() {

    bills = manager.getBills();

    tvSales.getItems().clear();

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
    if(logoutConfirmation()) {
      controller.setLoginPane();
    }
  }

  private boolean logoutConfirmation() {
    Optional<ButtonType> result = DialogUtils.confirmationDialog("Logout", "Are you sure?");
    return result.get() == ButtonType.OK;
  }

  public void bShowClick(ActionEvent event) {

    clearLabels();

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

    if(manager.getWorkers() == null) {
      manager.downloadWorkers();
    }

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

    manager.downloadProducts();

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

    clearLabels();
    manager.downloadBills(billChoice);
    tvBill.getItems().clear();
    addToSaleList();
  }

  public void bDeleteSaleClick(ActionEvent event) {

    clearLabels();

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


  public void bViewLogsClick(ActionEvent event) {
    setViewLogsPane();
  }

  private void setViewLogsPane() {
    FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxmlFiles/ViewLogsPane.fxml"));
    AnchorPane viewLogsPane = null;
    try {
      viewLogsPane = loader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }
    ViewLogsPaneController viewLogsController = loader.getController();
    viewLogsController.setController(controller);
    viewLogsController.setLoginController(loginController);
    viewLogsController.setManager(manager);
    viewLogsController.addToLogsList();
    controller.setPane(viewLogsPane);
  }

  public void bChangePswdClick(ActionEvent event) {
    setChangePswdPane();
  }

  private void setChangePswdPane() {
    FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxmlFiles/ChangePswdPane.fxml"));
    AnchorPane changePswdPane = null;
    try {
      changePswdPane = loader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }
    ChangePswdController changePswdController = loader.getController();
    changePswdController.setController(controller);
    changePswdController.setLoginController(loginController);
    changePswdController.setWorkerType("manager");
    changePswdController.setWorker(manager);
    controller.setPane(changePswdPane);
  }


  public void bBackupClick(ActionEvent event) {

    clearLabels();

    String path = "";

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");
    LocalDateTime now = LocalDateTime.now();
    System.out.println(dtf.format(now));

    try {
      path = Paths.get(this.getClass().getResource("/backup").toURI()).toString() + "\\" + dtf.format(now) + ".sql";
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }

    Backup backup = new Backup();

    if(!backup.executeBackup(path,username,password)){
      lError.setVisible(true);
    } else {
      lSuccessB.setVisible(true);
    }
  }

  public void bRestoreClick(ActionEvent event) {

    clearLabels();

    Backup backup = new Backup();

    String user = "root";
    String pass = "root";


    if(!backup.restoreBackup(username,password)){
      lError.setVisible(true);
    } else {
      lSuccessR.setVisible(true);
    }
  }

  public void clearLabels() {
    lError.setVisible(false);
    lSuccessB.setVisible(false);
    lSuccessR.setVisible(false);
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}

