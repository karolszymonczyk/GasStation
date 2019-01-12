package controllers;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import workers.Worker;


public class ChangePswdController {

  public PasswordField pfNewPswd1;
  public PasswordField pfNewPswd2;
  public Button bChange;
  public Label lError;
  public Label lSuccess;
  MainController controller;
  LoginPaneController loginController;
  ManagerPaneController managerController;

  int workerID;

  workers.Worker worker;

  String workerType;

  @FXML
  public void initialize() {
    Application.setUserAgentStylesheet(Application.STYLESHEET_CASPIAN);
  }

  public void setController(MainController controller) {
    this.controller = controller;
  }

  public void setLoginController(LoginPaneController loginController) {
    this.loginController = loginController;
  }

  public void setManagerController(ManagerPaneController managerController) {
    this.managerController = managerController;
  }

  public void setWorkerType(String workerType) {
    this.workerType = workerType;
  }

  public void bBackClick(ActionEvent event) {
    switch (workerType) {
      case "manager":
        loginController.setManagerPane();
        break;
      case "seller":
        loginController.setSellerPane("");
        break;
      case "storekeeper":
        loginController.setStorekeeperPane("", null);
        break;

        default:
          managerController.setViewWorkersPane();
          break;
    }
  }

  public void bChangeClick(ActionEvent event) {

    lError.setVisible(false);
    lSuccess.setVisible(false);

    if(pfNewPswd1.getText().equals("") || pfNewPswd2.getText().equals("")) {
      lError.setText("Empty field!");
      lError.setVisible(true);
      return;
    }

    if(!pfNewPswd1.getText().equals(pfNewPswd2.getText())) {
      lError.setText("Different passwords!");
      lError.setVisible(true);
      return;
    }

    if(workerType.equals("")){
      worker.changePasswordAsManager(workerID, pfNewPswd1.getText());
    } else {
      worker.changePassword(pfNewPswd1.getText());
    }

    lSuccess.setVisible(true);
    pfNewPswd1.setDisable(true);
    pfNewPswd2.setDisable(true);
    bChange.setDisable(true);
  }

  public void setWorker(Worker worker) {
    this.worker = worker;
  }

  public void setWorkerID(int workerID) {
    this.workerID = workerID;
  }
}
