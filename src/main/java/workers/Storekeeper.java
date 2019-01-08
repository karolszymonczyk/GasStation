
package workers;

import elements.ProductForDeliver;

import java.sql.*;
import java.util.ArrayList;

public class Storekeeper extends Worker{

  private boolean TransactionStarted = false;

  private ArrayList<ProductForDeliver> deliveredProducts;

  public Storekeeper(Connection connection){
    this.connection = connection;
    deliveredProducts = new ArrayList<>();
  }

  public boolean isTransactionStarted() {
    return TransactionStarted;
  }

  public void setTransactionStarted(boolean TransactionStarted) {
    this.TransactionStarted = TransactionStarted;
  }

//

  public void deleteDelivery() {
    try {
      cSt = connection.prepareCall("{CALL deleteDelivery()}");
      cSt.executeQuery();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

//  public void endDelivery() {
//    try {
//      cSt = connection.prepareCall("{CALL endDelivery()}");
//      cSt.executeQuery();
//    } catch (SQLException e) {
//      e.printStackTrace();
//    }
//  }

  public ArrayList<ProductForDeliver> getDeliveredProducts() {
    return deliveredProducts;
  }

  public void addDeliveryProduct(ProductForDeliver product){
    deliveredProducts.add(product);
  }

//  public void setDeliverer(String deliverer){
//    try {
//      cSt = connection.prepareCall("{CALL setDeliverer(?)}");
//      cSt.setString(1,deliverer);
//      cSt.executeQuery();
//    } catch (SQLException e) {
//      e.printStackTrace();
//    }
//  }
}