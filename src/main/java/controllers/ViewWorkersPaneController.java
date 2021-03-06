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

public class ViewWorkersPaneController {

  public TableView tvWorkers;
  public TableColumn tvcId;
  public TableColumn tvcName;
  public TableColumn tvcSurname;
  public TableColumn tvcLogin;
  public TableColumn tvcStartContract;
  public TableColumn tvcEndContract;
  public TableColumn tvcJob;

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
    tvcStartContract.setCellValueFactory(new PropertyValueFactory<>("startContract"));
    tvcEndContract.setCellValueFactory(new PropertyValueFactory<>("endContract"));
    tvcJob.setCellValueFactory(new PropertyValueFactory<>("job"));
  }


    public void addWorkerList () {

      for (elements.Worker worker : manager.getWorkers()) {
        tvWorkers.getItems().add(worker);
      }
    }

    public void setController (MainController controller){
      this.controller = controller;
    }

    public void setLoginController (LoginPaneController LoginController){
      this.loginController = LoginController;
    }

    public void setManagerController (ManagerPaneController managerController){
      this.managerController = managerController;
    }

    public void bBackClick (ActionEvent event){
      loginController.setManagerPane();
    }

    public void bAddClick (ActionEvent event){
      FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxmlFiles/AddWorkerPane.fxml"));
      AnchorPane addProductPane = null;
      try {
        addProductPane = loader.load();
      } catch (IOException e) {
        e.printStackTrace();
      }
      AddWorkerPaneController addWorkerController = loader.getController();
      addWorkerController.setController(controller);
      addWorkerController.setManagerController(managerController);
      addWorkerController.setManager(manager);
      controller.setPane(addProductPane);
    }

    public void bDeleteClick (ActionEvent event){

      if(tvWorkers.getSelectionModel().getSelectedItem() == null) {
        return;
      }

      elements.Worker selectedItem = (elements.Worker) tvWorkers.getSelectionModel().getSelectedItem();
      tvWorkers.getItems().remove(selectedItem);
      manager.deleteUser(selectedItem.getId());
      manager.getWorkers().remove(selectedItem);
    }

    public void setManager (Manager manager){
      this.manager = manager;
    }

  public void bRefreshClick(ActionEvent event) {

    tvWorkers.getItems().clear();

    manager.downloadWorkers();
    addWorkerList();
  }

  public void changePswdClick(ActionEvent event) {

    Object selectedItem = tvWorkers.getSelectionModel().getSelectedItem();

    if(selectedItem == null) {
      return;
    }

    setChangePswdPane();
  }

  private void setChangePswdPane() {
    FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxmlFiles/ChangePswdPane.fxml"));
    AnchorPane changePswdPane = null;

    elements.Worker selectedItem = (elements.Worker) tvWorkers.getSelectionModel().getSelectedItem();

    try {
      changePswdPane = loader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }
    ChangePswdController changePswdController = loader.getController();
    changePswdController.setController(controller);
    changePswdController.setManagerController(managerController);
    changePswdController.setWorkerType("");
    changePswdController.setWorker(manager);
    changePswdController.setWorkerID(selectedItem.getId());
    controller.setPane(changePswdPane);
  }
}