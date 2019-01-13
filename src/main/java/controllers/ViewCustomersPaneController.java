package controllers;

import elements.BillElement;
import elements.Customer;
import elements.Deliver;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import workers.Manager;

import java.io.IOException;
import java.util.ArrayList;

public class ViewCustomersPaneController {
  public TableView tvCustomers;
  public TableColumn tvcId;
  public TableColumn tvcName;
  public TableColumn tvcSurname;
  public TableColumn tvcNIP;
  public TableColumn tvcCreated;

  Manager manager;

  MainController controller;
  LoginPaneController loginController;
  ManagerPaneController managerController;

  public void initialize() {
    Application.setUserAgentStylesheet(Application.STYLESHEET_CASPIAN);
    tvcId.setCellValueFactory(new PropertyValueFactory<>("id"));
    tvcName.setCellValueFactory(new PropertyValueFactory<>("name"));
    tvcSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));
    tvcNIP.setCellValueFactory(new PropertyValueFactory<>("NIP"));
    tvcCreated.setCellValueFactory(new PropertyValueFactory<>("created"));
  }

  public void bBackClick(ActionEvent event) {
    loginController.setManagerPane();
  }

  public void bAddClick(ActionEvent event) {
    FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxmlFiles/AddNewCustomerPane.fxml"));
    AnchorPane addNewCustomerPane = null;
    try {
      addNewCustomerPane = loader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }
    AddNewCustomerPaneController addNewCustomerController = loader.getController();
    addNewCustomerController.setController(controller);
    addNewCustomerController.setManagerController(managerController);
    addNewCustomerController.setViewCustomersController(this);
    addNewCustomerController.setManager(manager);
    controller.setPane(addNewCustomerPane);
  }

  public void bDeleteClick(ActionEvent event) {

    if(tvCustomers.getSelectionModel().getSelectedItem() == null) {
      return;
    }
    elements.Customer selectedItem = (elements.Customer) tvCustomers.getSelectionModel().getSelectedItem();
    tvCustomers.getItems().remove(selectedItem);

    manager.deleteCustomer(selectedItem.getId());
    manager.getCustomers().remove(selectedItem);
  }

  public void bRefreshClick(ActionEvent event) {

    manager.downloadCustomers();
    updateCustomerList(manager.getCustomers());

  }

  public void setController(MainController controller) {
    this.controller = controller;
  }

  public void setLoginController(LoginPaneController loginController) {
    this.loginController = loginController;
  }

  public void addCustomersList() {

    for(Customer customer : manager.getCustomers()) {
      tvCustomers.getItems().add(customer);
    }
  }

  public void setManagerController(ManagerPaneController managerController) {
    this.managerController = managerController;
  }

  public void setManager(Manager manager) {
    this.manager = manager;
  }

  void updateCustomerList(ArrayList<Customer> customers) {

    tvCustomers.getItems().clear();
    for (Customer customer : customers) {
      tvCustomers.getItems().add(customer);
    }
  }

}
