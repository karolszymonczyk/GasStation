package dbConnection;

import com.mysql.cj.exceptions.ConnectionIsClosedException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CreateConnection {

  private Connection connection;
  private String user;
  private String password;

  public CreateConnection(String user, String password) {

    this.user = user;
    this.password = password;
    this.connection = connector();
  }

  private Connection connector() {
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
    } catch (ClassNotFoundException e) {
      System.out.println("Where is your MySQL JDBC Driver?");
    }

    Connection connection = null;

    try {

      connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/stacjapaliw?verifyServerCertificate=false&useSSL=true", user, password);

    } catch (SQLException e) {
      System.out.println("Connection Failed.");
    }
    return connection;
  }

  Connection getConnection(){
    return connection;
  }
}
