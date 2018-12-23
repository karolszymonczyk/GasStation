package controllers;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class LoginPaneController {

  private MainController controller;

  private String login, password;

  @FXML
  private TextField tLogin, tPassword;

  @FXML
  private Button bLogin;

  @FXML
  public void initialize() {
    Application.setUserAgentStylesheet(Application.STYLESHEET_CASPIAN);
    //set disable true
  }

  public void setController(MainController controller) {
    this.controller = controller;
  }

  public void bLoginClick() {
    login = tLogin.getText();
    password = tPassword.getText();

    if(login.equals("manager")) {
      System.out.println("MANAGER");
      setManagerPane();
    } else if(login.equals("seller")) {
      System.out.println("SELLER");
      setSellerPane();
    } else if(login.equals("storekeeper")) {
      System.out.println("STOREKEEPER");
      setStorekeeperPane();
    }
  }

  private void setManagerPane() {
    FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxmlFiles/ManagerPane.fxml"));
    AnchorPane managerPane = null;
    try {
      managerPane = loader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }
    ManagerPaneController managerController = loader.getController();
    managerController.setController(controller);
    controller.setPane(managerPane);
  }

  private void setSellerPane() {
    FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxmlFiles/SellerPane.fxml"));
    AnchorPane sellerPane = null;
    try {
      sellerPane = loader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }
    SellerPaneController sellerController = loader.getController();
    sellerController.setController(controller);
    controller.setPane(sellerPane);
  }

  private void setStorekeeperPane() {
    FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxmlFiles/StorekeeperPane.fxml"));
    AnchorPane storekeeperPane = null;
    try {
      storekeeperPane = loader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }
    StorekeeperPaneController storekeeperController = loader.getController();
    storekeeperController.setController(controller);
    controller.setPane(storekeeperPane);
  }
}
