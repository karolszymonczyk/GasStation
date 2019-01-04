package controllers;

import elements.Worker;
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

//TODO DODAC KOLUMNE Z KONCEM

public class ViewWorkersPaneController {

  public TableView tvWorkers;
  public TableColumn tvcId;
  public TableColumn tvcName;
  public TableColumn tvcSurname;
  public TableColumn tvcLogin;
  public TableColumn tvcPassword;
  public TableColumn tvcStartContract;
  public TableColumn tvcEndContract;
  public TableColumn tvcJob;
  public TableColumn tvcStatus;

  private MainController controller;
  private LoginPaneController loginController;
  private ManagerPaneController managerController;

  Manager manager;

  @FXML
  public void initialize() {
    Application.setUserAgentStylesheet(Application.STYLESHEET_CASPIAN);
    tvcId.setCellValueFactory(new PropertyValueFactory<>("id"));
    tvcName.setCellValueFactory(new PropertyValueFactory<>("name"));
    tvcSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));
    tvcLogin.setCellValueFactory(new PropertyValueFactory<>("login"));
    tvcPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
    tvcStartContract.setCellValueFactory(new PropertyValueFactory<>("startContract"));
    tvcEndContract.setCellValueFactory(new PropertyValueFactory<>("endContract"));
    tvcJob.setCellValueFactory(new PropertyValueFactory<>("job"));
    tvcStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    //addToWorkersList();
  }

//  private void addToWorkersList() {
//    addWorker(1, "Marcin", "Zapała", "marcin.zapala", "12345", "03-01-2019", "03-01-2031" ,"seller", "working");
//  }
//
  public void addWorkerList() {

    for(elements.Worker worker : manager.getWorkers())
    tvWorkers.getItems().add(worker);
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
    FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxmlFiles/AddWorkerPane.fxml"));
    AnchorPane addProductPane = null;
    try {
      addProductPane = loader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }
    AddWorkerPaneController addProductController = loader.getController();
    addProductController.setController(controller);
    addProductController.setManagerController(managerController);
    controller.setPane(addProductPane);
  }

  public void bDeleteClick(ActionEvent event) {
    Object selectedItem = tvWorkers.getSelectionModel().getSelectedItem();
    tvWorkers.getItems().remove(selectedItem);
  }

  public void setManager(Manager manager) {
    this.manager = manager;
  }
}