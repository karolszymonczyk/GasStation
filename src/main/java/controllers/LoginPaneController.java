package controllers;

import dbConnection.LoginCheck;
import workers.Manager;
import workers.Seller;
import workers.Storekeeper;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
//
//TODO zaminić password na PasswordLabel

public class LoginPaneController {

  public Label lError;
  private MainController controller;


  private Seller seller;
  private Storekeeper storeKeeper;
  private Manager manager;

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
    lError.setVisible(false);

    login = tLogin.getText();
    password = tPassword.getText();
    LoginCheck loginCheck = new LoginCheck(login,password);

    if(loginCheck.correctUserAndPass()) {
      if ("manager".equals(loginCheck.job)) {
        manager = new Manager(loginCheck.getConnection());
        setManagerPane();

      } else if ("seller".equals(loginCheck.job)) {
        seller = new Seller(loginCheck.getConnection());
        setSellerPane();
        //seller.createBill();

      } else if ("storekeeper".equals(loginCheck.job)) {//TODO test.storekeeper pass: sk
        storeKeeper = new Storekeeper(loginCheck.getConnection());
        setStorekeeperPane();
      } else {
        System.out.println("DATABASE ERROR");

      }
    }
  }

  public void setManagerPane() {
    FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxmlFiles/ManagerPane.fxml"));
    AnchorPane managerPane = null;
    try {
      managerPane = loader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }
    ManagerPaneController managerController = loader.getController();
    managerController.setManager(manager);
    managerController.setController(controller);
    managerController.setLoginController(this);
    managerController.addToSaleList();
    controller.setPane(managerPane);
  }

  public void setSellerPane() {
    FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxmlFiles/SellerPane.fxml"));
    AnchorPane sellerPane = null;
    try {
      sellerPane = loader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }
    SellerPaneController sellerController = loader.getController();
    sellerController.setSeller(seller);
    sellerController.setController(controller);
    sellerController.setLoginController(this);
    sellerController.addToProductList(seller.getProducts());

    controller.setPane(sellerPane);
  }

  public void setStorekeeperPane() {
    FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxmlFiles/StorekeeperPane.fxml"));
    AnchorPane storekeeperPane = null;
    try {
      storekeeperPane = loader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }
    StorekeeperPaneController storekeeperController = loader.getController();
    storekeeperController.setStoreKeeper(storeKeeper);
    storekeeperController.setController(controller);
    storekeeperController.setLoginController(this);
    storekeeperController.addToList(storeKeeper.getDeliveredProducts());
    controller.setPane(storekeeperPane);
  }
}
