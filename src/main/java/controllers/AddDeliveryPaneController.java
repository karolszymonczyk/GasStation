package controllers;

import elements.ProductForDeliver;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import workers.Manager;

import java.io.IOException;
import java.sql.SQLException;

public class AddDeliveryPaneController extends StorekeeperPaneController {

  Manager manager;

  ManagerPaneController managerController;
  ViewDeliversPaneController viewDeliversController;

  @Override
  public void bLogoutClick(ActionEvent event) {
    managerController.setViewDeliversPane();
    manager.downloadDeliveries();
  }

  void setManagerController(ManagerPaneController managerController) {
    this.managerController = managerController;
  }

  void setViewDeliversPaneController(ViewDeliversPaneController viewDeliversController) {
    this.viewDeliversController = viewDeliversController;
  }

  @Override
  public void bNewProductClick(ActionEvent event) {
    setAddNewProductPane();
  }

  private void setAddNewProductPane() {
    FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxmlFiles/AddNewProductPane.fxml"));
    AnchorPane addNewProductPane = null;
    try {
      addNewProductPane = loader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }
    AddNewProductPaneController addNewProductController = loader.getController();
    addNewProductController.setController(controller);
    addNewProductController.setLoginController(loginController);
    addNewProductController.setDeliverer(tfDeliverer.getText());
    addNewProductController.setViewDeliversController(viewDeliversController);
    controller.setPane(addNewProductPane);
  }

  public void setManager(Manager manager) {
    this.manager = manager;
  }

  @Override
  public void bAddClick(ActionEvent event) {

    disableButtons(false);

    lError.setVisible(false);
    lSuccess.setVisible(false);

    deliverer = tfDeliverer.getText();
    String code = tfCode.getText();
    String amount = tfAmount.getText();

    int amountInt;
    int codeInt;
    amountInt = Integer.parseInt(amount);
    codeInt = Integer.parseInt(code);

    String name = manager.getProductName(codeInt);

    ProductForDeliver product = new ProductForDeliver(name, code, amountInt);
    manager.addDeliveryProduct(product);

    manager.existingProductDeliver(Integer.parseInt(code),Integer.parseInt(amount),deliverer);

    lSuccess.setVisible(true);

    addToList(manager.getDeliveredProducts());
  }

}
