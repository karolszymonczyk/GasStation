package controllers;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
//
public class CardPaneController {

  public TextField tfName;
  public TextField tfSurname;
  public TextField taNIP;
  public Label lError;
  public Label lSucces;
  public Button bCreate;
  private MainController controller;
  private LoginPaneController loginController;

  @FXML
  public void initialize() {
    Application.setUserAgentStylesheet(Application.STYLESHEET_CASPIAN);
  }

  public void setController(MainController controller) {
    this.controller = controller;
  }

  public void setLoginController(LoginPaneController LoginController) {
    this.loginController = LoginController;
  }

  public void bBackClick(ActionEvent event) {
    loginController.setSellerPane();
  }

  public void bCreateClick(ActionEvent event) {

    lError.setVisible(false);

    String name = tfName.getText();
    String surname = tfSurname.getText();
    String NIP = taNIP.getText();

    if(!checkFormat(NIP)) {
     lError.setVisible(true);
     return;
    }

    //TODO po przejsciu dodać kartę do bazy danych

    lSucces.setVisible(true);
    setDisbledPane();
  }

  private void setDisbledPane() {
    tfName.setDisable(true);
    tfSurname.setDisable(true);
    taNIP.setDisable(true);
    bCreate.setDisable(true);
  }

  private boolean checkFormat(String check) {
    int i;
    try {
      i = Integer.parseInt(check);
    } catch (NumberFormatException e) {
      return false;
    }
    return true;
  }
}
