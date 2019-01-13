package controllers;

//import dbConnection.DownloadThread;
import dbConnection.LoginCheck;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import workers.Manager;
import workers.Seller;
import workers.Storekeeper;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayList;

public class LoginPaneController {

  public Label lError;
  private MainController controller;


  private Seller seller;
  private Storekeeper storekeeper;
  private Manager manager;

  private String login, password;

//  ArrayList<DownloadThread> threads = new ArrayList<DownloadThread>();

  @FXML
  private TextField tLogin, tPassword;

  @FXML
  private Button bLogin;

  @FXML
  public void initialize() {
    Application.setUserAgentStylesheet(Application.STYLESHEET_CASPIAN);
  }

  public void setController(MainController controller) {
    this.controller = controller;
  }

  public void bLoginClick() {
    lError.setVisible(false);

    login = tLogin.getText();
    password = tPassword.getText();
    LoginCheck loginCheck = new LoginCheck(login, password);
    try {
      if (loginCheck.correctUserAndPass()) {
        if ("manager".equals(loginCheck.job)) {
          manager = new Manager(loginCheck.getConnection());
          setManagerPane();



        } else if ("seller".equals(loginCheck.job)) {
          seller = new Seller(loginCheck.getConnection());
          setSellerPane("");
        } else if ("storekeeper".equals(loginCheck.job)) {
          storekeeper = new Storekeeper(loginCheck.getConnection());
          setStorekeeperPane("",null);
        }
      }
    } catch (SQLException e) {
      lError.setVisible(true);
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
    managerController.setUsername(login);
    managerController.setPassword(password);




    controller.setPane(managerPane);
  }

  void setSellerPane(String customer) {
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
    sellerController.setCustomer(customer);
    sellerController.addToProductList(seller.getProducts());
    sellerController.updateBillList(seller.getActiveBill());
    if (seller.getActiveBill().size() == 0) {
      sellerController.disableButtons(true);
    }

    controller.setPane(sellerPane);
  }

  void setStorekeeperPane(String deliverer, Savepoint delete) {
    FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxmlFiles/StorekeeperPane.fxml"));
    AnchorPane storekeeperPane = null;
    try {
      storekeeperPane = loader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }
    StorekeeperPaneController storekeeperController = loader.getController();
    storekeeperController.setStoreKeeper(storekeeper);
    storekeeperController.setController(controller);
    storekeeperController.setLoginController(this);
    storekeeperController.setDeliverer(deliverer);
    storekeeperController.addToList(storekeeper.getDeliveredProducts());
    storekeeperController.setDelete(delete);
    if(storekeeper.getDeliveredProducts().size() == 0){
      storekeeperController.disableButtons(true);
    } else
      storekeeperController.disableButtons(false);

    controller.setPane(storekeeperPane);
  }

  public String getLogin() {
    return login;
  }

  public String getPassword() {
    return password;
  }
}
