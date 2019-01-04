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
}

