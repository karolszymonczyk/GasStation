package workers;

import elements.BillElement;

import java.sql.*;
import java.util.ArrayList;

public abstract class Worker {

  Connection connection;
  CallableStatement cSt;
  Statement st;
  ResultSet rs;
  private ArrayList<BillElement> activeBill = new ArrayList<>();

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

  public boolean searchForProductFromName(String name) {
    try {
      cSt = connection.prepareCall("{? = CALL searchProductFromName(?)}");
      cSt.setString(2, name);
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

  public double getPrice(int code) {

    double price = 0;

    try {
      st = connection.createStatement();
      rs = st.executeQuery("SELECT price FROM product WHERE code =" + code);
      while(rs.next()) {
        price = rs.getFloat("price");
        round(price);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return round(price);
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

  public void addToBill(double value){
    try {
      cSt = connection.prepareCall("{CALL billValueUpdate(?)}");
      cSt.setFloat(1,(float)value);
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
    activeBill.clear();
  }

  public void deleteBill(){
    try {
      cSt = connection.prepareCall("{CALL deleteBill()}");
      cSt.executeQuery();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public boolean existingProductDeliver(int code, int amount){
    try {
      cSt = connection.prepareCall("{CALL existingProductDelivery(?,?)}");
      cSt.setInt(1,code);
      cSt.setInt(2,amount);
      cSt.execute();
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  public boolean addNewProduct(int code, String name, float price, float tax, int amount){
    try {
      cSt = connection.prepareCall("{CALL newProductDelivery(?,?,?,?,?)}");
      cSt.setInt(1,code);
      cSt.setString(2,name.toString());
      cSt.setFloat(3,price);
      cSt.setInt(4,amount);
      cSt.setFloat(5,tax);
      cSt.execute();
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  public void createDelivery() {
    try {
      cSt = connection.prepareCall("{CALL createDelivery()}");
      cSt.execute();
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

  public void setDeliverer(String deliverer){
    try {
      cSt = connection.prepareCall("{CALL setDeliverer(?)}");
      cSt.setString(1,deliverer.toLowerCase());
      cSt.executeQuery();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void cancelDelivery() {
    try {
      cSt = connection.prepareCall("{CALL deleteDelivery()}");
      cSt.executeQuery();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public double downloadDiscount(int NIP){

    double discount = -1;

    try {
      cSt = connection.prepareCall("{? = CALL getDiscount(?)}");
      cSt.setInt(2, NIP);
      cSt.registerOutParameter(1, Types.FLOAT);
      cSt.execute();
      discount = cSt.getFloat(1);
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return discount;
  }

  public double round(Double number){
    return Math.round(number*1e2)/1e2;
  }

  public void addToActiveBill(BillElement billElement){
    activeBill.add(billElement);
  }

  public ArrayList<BillElement> getActiveBill() {
    return activeBill;
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

  public void changePassword(String password){

  }

  public void changePasswordAsManager(int ID, String password){

  }

 }

