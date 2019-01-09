package controllers;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import utils.ErrorUtils;
import workers.Manager;

public class AddNewCustomerPaneController {

  public TextField tfName;
  public TextField tfSurname;
  public TextField taNIP;
  public Button bCreate;
  public Label lError;
  public Label lSuccess;
  MainController controller;
  ManagerPaneController managerController;
  ViewCustomersPaneController viewCustomersController;

  Manager manager;

  public void initialize() {
    Application.setUserAgentStylesheet(Application.STYLESHEET_CASPIAN);
  }

  public void bBackClick(ActionEvent event) {
    managerController.setViewCustomersPane();
  }

  public void bCreateClick(ActionEvent event) {
    lError.setVisible(false);

    String name = tfName.getText();
    String surname = tfSurname.getText();
    String sNIP = taNIP.getText();

    if(name.equals("") || surname.equals("") || sNIP.equals("")) {
      lError.setVisible(true);
      return;
    }

    if(!ErrorUtils.checkInt(sNIP)) {
      lError.setVisible(true);
      return;
    }

    int iNIP = Integer.parseInt(sNIP);

    manager.addNewCustomer(name, surname, iNIP);

    lSuccess.setVisible(true);
    setDisabledPane();
  }

  public void setController(MainController controller) {
    this.controller = controller;
  }

  public void setManagerController(ManagerPaneController managerController) {
    this.managerController = managerController;
  }

  private void setDisabledPane() {
    tfName.setDisable(true);
    tfSurname.setDisable(true);
    taNIP.setDisable(true);
    bCreate.setDisable(true);
  }

  public void setManager(Manager manager) {
    this.manager = manager;
  }

  public void setViewCustomersController(ViewCustomersPaneController viewDeliversPaneController) {
    this.viewCustomersController = viewDeliversPaneController;
  }
}
