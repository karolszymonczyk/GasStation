package controllers;

import elements.Log;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ViewLogsPaneController {

  public TableView tvLogs;
  public TableColumn tvcId;
  public TableColumn tvcName;
  public TableColumn tvcSurname;
  public TableColumn tvcOperation;
  public TableColumn tvcTime;
  MainController controller;
  LoginPaneController loginController;

  @FXML
  public void initialize() {
    Application.setUserAgentStylesheet(Application.STYLESHEET_CASPIAN);
    tvcId.setCellValueFactory(new PropertyValueFactory<>("id"));
    tvcName.setCellValueFactory(new PropertyValueFactory<>("name"));
    tvcSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));
    tvcOperation.setCellValueFactory(new PropertyValueFactory<>("operation"));
    tvcTime.setCellValueFactory(new PropertyValueFactory<>("time"));
  }

  public void setController(MainController controller) {
    this.controller = controller;
  }

  public void setLoginController(LoginPaneController loginController) {
    this.loginController = loginController;
  }

  public void bBackClick(ActionEvent event) {
    loginController.setManagerPane();
  }

  public void bRefreshClick(ActionEvent event) {
  }

  public void addToLogsList(){
    //TODO tutaj korzystaj tak jak wszędzie indziej XD tylko masz klase Log
//    Log log = new Log(id, name, surname, operation, time);
//    tvLogs.getItems().add(log);
  }
}
