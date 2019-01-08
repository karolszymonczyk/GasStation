package workers;

import elements.ProductForDeliver;

import java.sql.*;
import java.util.ArrayList;

public class Storekeeper extends Worker{

  boolean transactionStarted = false;

  ArrayList<ProductForDeliver> deliveredProducts;


  public Storekeeper(Connection connection){
    this.connection = connection;
    deliveredProducts = new ArrayList<>();
  }

//  public boolean existingProductDeliver(int code, int amount, String deliverer){
//    try {
//      cSt = connection.prepareCall("{CALL existingProductDelivery(?,?,?)}");
//      cSt.setInt(1,code);
//      cSt.setInt(2,amount);
//      cSt.setString(3,deliverer);
//      cSt.execute();
//    } catch (SQLException e) {
//      return false;
//    }
//    return true;
//  }

//  public boolean addNewProduct(int code, String name, float price, float tax, int amount, String deliverer){
//    try {
//      cSt = connection.prepareCall("{CALL newProductDelivery(?,?,?,?,?,?)}");
//      cSt.setInt(1,code);
//      cSt.setString(2,name);
//      cSt.setFloat(3,price);
//      cSt.setInt(4,amount);
//      cSt.setFloat(5,tax);
//      cSt.setString(6,deliverer);
//      cSt.execute();
//    } catch (SQLException e) {
//      e.printStackTrace();
//      return false;
//    }
//    return true;
//  }

  public boolean isTranactionStarted() {
    return transactionStarted;
  }

  public void setTranactionStarted(boolean tranactionStarted) {
    this.transactionStarted = tranactionStarted;
  }

  public void createDelivery() {
    try {
    cSt = connection.prepareCall("{CALL createDelivery()}");
    cSt.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void deleteDelivery() {
    try {
      cSt = connection.prepareCall("{CALL deleteDelivery()}");
      cSt.executeQuery();
      System.out.println("Usunalem dostawe");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void endDelivery() {
    try {
      cSt = connection.prepareCall("{CALL endDelivery()}");
      cSt.executeQuery();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public ArrayList<ProductForDeliver> getDeliveredProducts() {
    return deliveredProducts;
  }

  public void addDeliveryProduct(ProductForDeliver product){
    deliveredProducts.add(product);
  }
}
