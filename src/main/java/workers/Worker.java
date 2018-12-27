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
}
