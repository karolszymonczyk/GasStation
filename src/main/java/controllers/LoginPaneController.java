package controllers;

import dbConnection.LoginCheck;
import workers.Seller;
import workers.StoreKeeper;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class LoginPaneController {

  private MainController controller;

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
    String login = tLogin.getText();
    String password = tPassword.getText();
    LoginCheck loginCheck = new LoginCheck(login,password);

    if(loginCheck.correctUserAndPass()) {
      switch (loginCheck.job) {
        case "manager":
          setManagerPane();
          break;
        case "seller":
          setSellerPane();
          Seller seller = new Seller(loginCheck.getConnection());
          break;
        case "storekeeper":
          //TODO test.storekeeper pass: sk
          setStorekeeperPane();
          StoreKeeper storeKeeper = new StoreKeeper(loginCheck.getConnection());
          break;
        default:
          System.out.println("DATABASE ERROR");
          break;
      }
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
