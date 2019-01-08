package controllers;

import elements.ProductForDeliver;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import utils.ErrorUtils;
import workers.Manager;

import java.io.IOException;
import java.sql.SQLException;

public class AddDeliveryPaneController extends StorekeeperPaneController implements ErrorUtils {

  Manager manager;

  ManagerPaneController managerController;
  ViewDeliversPaneController viewDeliversController;

  @Override
  public void bLogoutClick(ActionEvent event) {

        try {
          manager.getConnection().rollback();
          manager.getConnection().setAutoCommit(true);
        } catch (SQLException e) {
          System.out.println("No active transaction - no rollback.");
        }
        manager.cancelDelivery();
        controller.setLoginPane();

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
    addNewProductController.setManager(manager);
    controller.setPane(addNewProductPane);
  }

  public void setManager(Manager manager) {
    this.manager = manager;
  }

  @Override
  public void bAddClick(ActionEvent event) {
    
    lError.setVisible(false);
    lSuccess.setVisible(false);

    disableButtons(false);

    deliverer = tfDeliverer.getText();
    String code = tfCode.getText();
    String amount = tfAmount.getText();

    if(code.equals("") || amount.equals("")) {
      lError.setVisible(true);
    }

    if(!ErrorUtils.checkInt(code) || !ErrorUtils.checkInt(amount) || !storekeeper.searchForProductFromCode(Integer.parseInt(code))) {

      lError.setVisible(true);
      return;
    }

    try {
      delete = manager.getConnection().setSavepoint("delete");
    } catch (SQLException e) {
      e.printStackTrace();
    }

    int amountInt;
    int codeInt;
    amountInt = Integer.parseInt(amount);
    codeInt = Integer.parseInt(code);

    if(!manager.isTransactionStarted()){
      manager.setTransactionStarted(true);

      try {
        manager.getConnection().setAutoCommit(false);
        System.out.println("Zacząłem tranze");
        manager.createDelivery();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }

    String name = manager.getProductName(codeInt);

    ProductForDeliver product = new ProductForDeliver(name, code, amountInt);
    manager.addDeliveryProduct(product);

    manager.existingProductDeliver(Integer.parseInt(code),Integer.parseInt(amount));

    lSuccess.setVisible(true);

    addToList(manager.getDeliveredProducts());

    tfCode.setText("");
    tfAmount.setText("");
  }

  @Override
  public void bFinishClick(ActionEvent event) {

    disableButtons(true);

    lError.setVisible(false);
    lSuccess.setVisible(false);
    manager.setTransactionStarted(false);

    try {

      manager.setDeliverer(tfDeliverer.getText());
      manager.endDelivery();
      System.out.println("USTAWIAM DELIVERERA = " + tfDeliverer.getText());
      manager.getConnection().commit();
      System.out.println("SKOńczyłem tranze");
      manager.getConnection().setAutoCommit(true);

    } catch (SQLException e) {
      e.printStackTrace();
    }
    manager.getDeliveredProducts().clear();
    tvProducts.getItems().clear();
  }

  @Override
  public void bDeleteClick(ActionEvent event) {
    lError.setVisible(false);
    lSuccess.setVisible(false);
    try {
      manager.getConnection().rollback(delete);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    Object selectedItem = tvProducts.getSelectionModel().getSelectedItem();
    tvProducts.getItems().remove(selectedItem);
    if(tvProducts.getItems().isEmpty()){
      disableButtons(true);
    }
  }
}

