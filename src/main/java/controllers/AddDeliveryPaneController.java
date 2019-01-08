package controllers;

import elements.ProductForDeliver;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import utils.DialogUtils;
import utils.ErrorUtils;
import workers.Manager;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.Optional;

public class AddDeliveryPaneController extends StorekeeperPaneController implements ErrorUtils {

  Manager manager;

  ManagerPaneController managerController;
  ViewDeliversPaneController viewDeliversController;

  private Savepoint deleted;


  @Override
  public void bLogoutClick(ActionEvent event) {

        try {
          manager.getConnection().rollback();
          manager.getConnection().setAutoCommit(true);
          manager.setTransactionStarted(false);
        } catch (SQLException e) {
          System.out.println("No active transaction - no rollback.");
        }
        manager.cancelDelivery();
        controller.setLoginPane();
    manager.getDeliveredProducts().clear();
    managerController.setViewDeliversPane();
    manager.downloadDeliveries();
  }

  void setManagerController(ManagerPaneController managerController) {
    this.managerController = managerController;
  }

  void setViewDeliversPaneController(ViewDeliversPaneController viewDeliversController) {
    this.viewDeliversController = viewDeliversController;
  }

  private void noDeliverer() {
    Optional<ButtonType> info = DialogUtils.informationDialog("Empty field", "Deliverer field is empty. Please insert value");
  }

  @Override
  public void bNewProductClick(ActionEvent event) {
    setAddNewProductPane();
    try {
      deleted = manager.getConnection().setSavepoint("delete");
    } catch (SQLException e) {
      e.printStackTrace();
    }
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
    addNewProductController.setAddDeliveryController(this);
    addNewProductController.setDeleted(deleted);
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

    if(!ErrorUtils.checkInt(code) || !ErrorUtils.checkInt(amount) || !manager.searchForProductFromCode(Integer.parseInt(code))) {

      lError.setVisible(true);
      return;
    }

    int amountInt;
    int codeInt;
    amountInt = Integer.parseInt(amount);
    codeInt = Integer.parseInt(code);

    if(!manager.isTransactionStarted()){
      manager.setTransactionStarted(true);

      try {
        manager.getConnection().setAutoCommit(false);
        manager.createDelivery();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }

    String name = manager.getProductName(codeInt);

    ProductForDeliver product = new ProductForDeliver(name, code, amountInt);

    try {
      deleted = manager.getConnection().setSavepoint("delete");
    } catch (SQLException e) {
      e.printStackTrace();
    }


    manager.addDeliveryProduct(product);

    manager.existingProductDeliver(Integer.parseInt(code),Integer.parseInt(amount));

    lSuccess.setVisible(true);

    addToList(manager.getDeliveredProducts());

    tfCode.setText("");
    tfAmount.setText("");
  }

  @Override
  public void bFinishClick(ActionEvent event) {

    if(tfDeliverer.getText().equals("")) {
      noDeliverer();
      return;
    }

    disableButtons(true);

    lError.setVisible(false);
    lSuccess.setVisible(false);
    manager.setTransactionStarted(false);

    try {

      manager.setDeliverer(tfDeliverer.getText());
      manager.endDelivery();
      manager.getConnection().commit();
      manager.getConnection().setAutoCommit(true);

    } catch (SQLException e) {
      e.printStackTrace();
    }
    manager.getDeliveredProducts().clear();
    tvProducts.getItems().clear();
    tfDeliverer.setText("");
  }

  @Override
  public void bDeleteClick(ActionEvent event) {
    lError.setVisible(false);
    lSuccess.setVisible(false);
    try {
      manager.getConnection().rollback(deleted);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    Object selectedItem = tvProducts.getSelectionModel().getSelectedItem();
    manager.getDeliveredProducts().remove(selectedItem);
    tvProducts.getItems().remove(selectedItem);
    if(tvProducts.getItems().isEmpty()){
      disableButtons(true);
    }
  }

  public void loadActiveDelivery(ArrayList<ProductForDeliver> deliveredProducts) {

    tvProducts.getItems().clear();

    for(ProductForDeliver productForDeliver : manager.getDeliveredProducts()){
      tvProducts.getItems().add(productForDeliver);
    }
  }

  public void setDeleted(Savepoint deleted) {
    this.deleted = deleted;
  }
}

