package dbConnection;

import java.sql.*;

public class LoginCheck {

  private Connection connection;
  private String inputUser;
  private String inputPassword;
  public String job;
  private ResultSet rs;
  private Statement st;

  public LoginCheck(String inputUser, String inputPassword) {
    this.inputUser = inputUser;
    this.inputPassword = inputPassword;
  }

  public boolean correctUserAndPass(){
    CreateConnection createConnection = new CreateConnection(inputUser,inputPassword);
    connection  = createConnection.getConnection();
    try {
      st = connection.createStatement();

      rs = st.executeQuery("SELECT * FROM users JOIN worker ON users.worker_id = worker.id WHERE login= \'" + inputUser + "\'&& password =\'" +inputPassword + "\'");
      System.out.println("przed petla");
      while(rs.next()){


            job = rs.getString("job");
            System.out.println("job = " + job);
            return true;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  public Connection getConnection() {
    return connection;
  }
}

