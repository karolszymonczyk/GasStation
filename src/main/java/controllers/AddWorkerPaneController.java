package controllers;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import workers.Manager;

import java.sql.Date;
import java.time.LocalDate;

//
public class AddWorkerPaneController {
  public TextField tfName;
  public TextField tfSurname;
  public ChoiceBox<String> cbJob;
  public DatePicker dpStart;
  public DatePicker dpEnd;
  public Button bCreate;
  public Label lError;
  public Label lSucces;

  Manager manager;

  private MainController controller;
  //  private LoginPaneController loginController;
  private ManagerPaneController managerController;

  @FXML
  public void initialize() {
    Application.setUserAgentStylesheet(Application.STYLESHEET_CASPIAN);
    cbJob.setValue("seller");
    cbJob.getItems().addAll("seller", "storekeeper", "manager");
    dpStart.setValue(LocalDate.now());
    dpEnd.setValue(LocalDate.now());
  }

  public void setController(MainController controller) {
    this.controller = controller;
  }

  public void setManagerController(ManagerPaneController managerController) {
    this.managerController = managerController;
  }

  public void bBackClick(ActionEvent event) {
    manager.downloadWorkers();
    managerController.setViewWorkersPane();
  }

  public void bCreateClick(ActionEvent event) {
    lError.setVisible(false);

    String name = tfName.getText();
    String surname = tfSurname.getText();
    String job = cbJob.getValue();
    String start = dpStart.getValue().toString();
    String end = dpEnd.getValue().toString();

    Date startD = null;
    Date endD = null;

    try{
      startD = Date.valueOf(start);
      endD = Date.valueOf(end);
    } catch (Exception e ){
      e.printStackTrace();
    }

    manager.createUser(name,surname,job, startD,endD);

    lSucces.setVisible(true);
    setDisbledPane();
  }

  private void setDisbledPane() {

    tfName.setDisable(true);
    tfSurname.setDisable(true);
    cbJob.setDisable(true);
    dpStart.setDisable(true);
    dpEnd.setDisable(true);
    bCreate.setDisable(true);
  }

  public void setManager(Manager manager) {
    this.manager = manager;
  }
}