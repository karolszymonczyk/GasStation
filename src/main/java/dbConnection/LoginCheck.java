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

  public boolean correctUserAndPass() throws SQLException{
      CreateConnection createConnection = new CreateConnection(inputUser,inputPassword);
      connection  = createConnection.getConnection();
      st = connection.createStatement();

      rs = st.executeQuery("SELECT * FROM users JOIN worker ON users.worker_id = worker.id WHERE login= \'" + inputUser + "\'");
      while(rs.next()){
        job = rs.getString("job");
        return true;
      }
    return false;
  }

  public Connection getConnection() {
    return connection;
  }

  public String getInputPassword() {
    return inputPassword;
  }

  public String getInputUser() {
    return inputUser;
  }
}

