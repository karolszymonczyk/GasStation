package workers;

import java.sql.*;

public abstract class Worker {

  Connection connection;
  CallableStatement cSt;
  Statement st;
  ResultSet rs;

  public void startTransaction(){
    try {
      connection.setAutoCommit(false);
    } catch (SQLException e) {
      System.out.println("Starting transaction failed.");
      e.printStackTrace();
    }
  }

  public void endTransaction(){
    try {
      connection.commit();
      connection.setAutoCommit(true);
    } catch (SQLException e) {
      System.out.println("Ending transaction failed.");
      e.printStackTrace();
    }
  }

  public void rollBack(){
      try {
        connection.rollback();
      } catch (SQLException e) {
        System.out.println("Rollback failed.");
        e.printStackTrace();
      }
  }
  public boolean searchForProductFromCode(int code) {
    try {
      cSt = connection.prepareCall("{? = CALL searchProductFromCode(?)}");
      cSt.setInt(2, code);
      cSt.registerOutParameter(1, Types.BOOLEAN);
      cSt.execute();
      return cSt.getBoolean(1);
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  public Connection getConnection() {
    return connection;
  }

  public String getProductName(int code) {
    String name= null;
    try {
      st = connection.createStatement();
      rs = st.executeQuery("SELECT name FROM product WHERE code = " + code);
      while(rs.next()) {
        name = rs.getString("name");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return name;
  }

  public void createBill(){
    try {
      cSt = connection.prepareCall("{CALL createBill()}");
      cSt.executeQuery();
    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println("ERROR");
    }
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

  public float getPrice(int code) {

    float price = 0;

    try {
      st = connection.createStatement();
      rs = st.executeQuery("SELECT price FROM product WHERE code =" + code);
      while(rs.next()) {
        price = rs.getFloat("price");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return price;
  }

  public void createSale(int code, int amount) {
    try {
      cSt = connection.prepareCall("{CALL addSale(?, ?)}");
      cSt.setInt(1,code);
      cSt.setInt(2,amount);
      cSt.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void addToBill(float value){
    try {
      cSt = connection.prepareCall("{CALL billValueUpdate(?)}");
      cSt.setFloat(1,value);
      cSt.executeQuery();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void closeBillWithoutCustomer() {
    try {
      cSt = connection.prepareCall("{CALL closeBillWithoutCustomer()}");
      cSt.executeQuery();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void closeBill(Integer NIP) {
    try {
      cSt = connection.prepareCall("{CALL closeBill(?)}");
      cSt.setInt(1,NIP);
      cSt.executeQuery();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void deleteBill(){
    try {
      cSt = connection.prepareCall("{CALL deleteBill()}");
      cSt.executeQuery();
      System.out.println("Usunalem rachunek");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public boolean existingProductDeliver(int code, int amount, String deliverer){
    try {
      cSt = connection.prepareCall("{CALL existingProductDelivery(?,?,?)}");
      cSt.setInt(1,code);
      cSt.setInt(2,amount);
      cSt.setString(3,deliverer);
      cSt.execute();
    } catch (SQLException e) {
      return false;
    }
    return true;
  }

  public boolean addNewProduct(int code, String name, float price, float tax, int amount, String deliverer){
    try {
      cSt = connection.prepareCall("{CALL newProductDelivery(?,?,?,?,?,?)}");
      cSt.setInt(1,code);
      cSt.setString(2,name);
      cSt.setFloat(3,price);
      cSt.setInt(4,amount);
      cSt.setFloat(5,tax);
      cSt.setString(6,deliverer);
      cSt.execute();
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }
}

