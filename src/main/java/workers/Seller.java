package workers;

import elements.Product;

import java.sql.*;
import java.util.ArrayList;

public class Seller extends Worker{

  private ArrayList<Product> products;
  private boolean transactionStarted = false;

  public Seller(Connection connection) {
    this.connection = connection;
    downloadProducts();
  }

//  public void createBill(){
//    try {
//      cSt = connection.prepareCall("{CALL createBill()}");
//      cSt.executeQuery();
//    } catch (SQLException e) {
//      e.printStackTrace();
//      System.out.println("ERROR");
//    }
//  }

//  public boolean searchForProductFromName(String name) {
//    try {
//      cSt = connection.prepareCall("{? = CALL searchProductFromName(?)}");
//      cSt.registerOutParameter(1, Types.BOOLEAN);
//      cSt.setString(2, name);
//      cSt.execute();
//      return cSt.getBoolean(1);
//    } catch (SQLException e) {
//      return false;
//    }
//  }

  public void sellProduct(int code, int amount) {
    try {
      cSt = connection.prepareCall("{CALL sellProduct(?.?)}");
      cSt.setInt(1, code);
      cSt.setInt(2, amount);
      cSt.execute();
    } catch (SQLException e) {

    }
  }

  private void downloadProducts() {

    products = new ArrayList<>();

    try {
      st = connection.createStatement();
      rs = st.executeQuery("SELECT name,code FROM product");

      while (rs.next()) {
        String name = rs.getString("name");
        String code = rs.getString("code");
        Product product = new Product(name, code);
        products.add(product);
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public ArrayList<Product> getProducts() {
    return products;
  }

  public int checkAmount(int code) {

    int amount = 0;

    try {
      st = connection.createStatement();
      rs = st.executeQuery("SELECT amount FROM product WHERE code = '" + code + "'");

      while (rs.next()) {
        amount = rs.getInt("amount");
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return amount;
  }

  public void addNewCustomer(String name, String surname, int NIP){
    try {
      cSt = connection.prepareCall("{CALL createNewCustomer(?, ?, ?)}");
      cSt.setString(1,name);
      cSt.setString(2,surname);
      cSt.setInt(3,NIP);

      cSt.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

//  public void createSale(int code, int amount) {
//    try {
//      cSt = connection.prepareCall("{CALL addSale(?, ?)}");
//      cSt.setInt(1,code);
//      cSt.setInt(2,amount);
//      cSt.execute();
//    } catch (SQLException e) {
//      e.printStackTrace();
//    }
//  }

//  public String getProductName(int code) {
//    String name= null;
//    try {
//      st = connection.createStatement();
//      rs = st.executeQuery("SELECT name FROM product WHERE code = " + code);
//      while(rs.next()) {
//        name = rs.getString("name");
//      }
//    } catch (SQLException e) {
//      e.printStackTrace();
//    }
//
//    return name;
//  }

//  public float getPrice(int code) {
//
//    float price = 0;
//
//    try {
//      st = connection.createStatement();
//      rs = st.executeQuery("SELECT price FROM product WHERE code =" + code);
//      while(rs.next()) {
//        price = rs.getFloat("price");
//      }
//    } catch (SQLException e) {
//      e.printStackTrace();
//    }
//
//    return price;
//  }

  public boolean isTransactionStarted() {
    return transactionStarted;
  }

  public void setTransactionStarted(boolean status){
    transactionStarted = status;
  }

//  public void closeBill(Integer NIP) {
//    try {
//      cSt = connection.prepareCall("{CALL closeBill(?)}");
//      cSt.setInt(1,NIP);
//      cSt.executeQuery();
//    } catch (SQLException e) {
//      e.printStackTrace();
//    }
//  }

//  public void deleteBill(){
//    try {
//      cSt = connection.prepareCall("{CALL deleteBill()}");
//      cSt.executeQuery();
//      System.out.println("Usunalem rachunek");
//    } catch (SQLException e) {
//      e.printStackTrace();
//    }
//  }

//  public void addToBill(float value){
//    try {
//      cSt = connection.prepareCall("{CALL billValueUpdate(?)}");
//      cSt.setFloat(1,value);
//      cSt.executeQuery();
//    } catch (SQLException e) {
//      e.printStackTrace();
//    }
//  }

//  public int findSaleID(int iCode, int intQuantity) {
//    try {
//      cSt = connection.prepareCall("{? = CALL findSaleID(?,?)}");
//      cSt.registerOutParameter(1, Types.INTEGER);
//      cSt.setInt(2,iCode);
//      cSt.setInt(3,intQuantity);
//      cSt.executeQuery();
//      return cSt.getInt(1);
//    } catch (SQLException e) {
//      e.printStackTrace();
//    }
//    return 0;
//  }

  public void deleteProduct(int saleID) {

    try {
      cSt = connection.prepareCall("{CALL deleteProduct(?)}");
      cSt.setInt(1,saleID);
      cSt.executeQuery();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

//  public void closeBillWithoutCustomer() {
//    try {
//      cSt = connection.prepareCall("{CALL closeBillWithoutCustomer()}");
//      cSt.executeQuery();
//    } catch (SQLException e) {
//      e.printStackTrace();
//    }
//  }
}
