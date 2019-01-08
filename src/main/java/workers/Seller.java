package workers;

import elements.BillElement;
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

  public boolean isTransactionStarted() {
    return transactionStarted;
  }

  public void setTransactionStarted(boolean status){
    transactionStarted = status;
  }

}
