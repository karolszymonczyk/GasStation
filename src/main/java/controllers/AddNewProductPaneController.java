package controllers;

import elements.ProductForDeliver;
import javafx.event.ActionEvent;
import utils.ErrorUtils;
import workers.Manager;

import java.sql.SQLException;

public class AddNewProductPaneController extends NewProductPaneController implements ErrorUtils{

  ViewDeliversPaneController viewDeliversController;
  Manager manager;

  public void setViewDeliversController(ViewDeliversPaneController viewDeliversController) {
    this.viewDeliversController = viewDeliversController;
  }

  @Override
  public void bBackClick(ActionEvent event) {
    viewDeliversController.setAddDeliveryPane(deliverer);
  }

  @Override
  public void bCreateClick(ActionEvent event) {
    lError.setVisible(false);

    String name = tfName.getText();
    String code = tfCode.getText();
    String price = tfPrice.getText();
    String tax = tfTax.getText();
    String amount = taAmount.getText();

    if(name.equals("") || code.equals("") || price.equals("") ||
            tax.equals("") || amount.equals("")) {
      lError.setVisible(true);
      return;
    }

    if(!ErrorUtils.checkInt(code) || !ErrorUtils.checkInt(amount)) {
      lError.setVisible(true);
      return;
    }

    if(!ErrorUtils.checkFloat(price) || !ErrorUtils.checkFloat(tax)) {
      lError.setVisible(true);
      return;
    }

    int amountInt = Integer.parseInt(amount);
    int codeInt = Integer.parseInt(code);
    System.out.println("MANAGER = " + manager);

    manager.addNewProduct(Integer.parseInt(code),name,Float.parseFloat(price),Float.parseFloat(tax),Integer.parseInt(amount));

    ProductForDeliver productForDeliver = new ProductForDeliver(manager.getProductName(codeInt),code,amountInt);
    manager.addDeliveryProduct(productForDeliver);

    lSuccess.setVisible(true);
    setDisabledPane();
  }

  public void setManager(Manager manager) {
    this.manager = manager;
  }
}
