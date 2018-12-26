package workers;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

public class Seller {

  private Connection connection;
  private CallableStatement cSt;

  public Seller(Connection connection){
    this.connection = connection;
  }

  public boolean searchForProductFromName(String name){
    try {
      cSt = connection.prepareCall("{? = CALL searchProductFromName(?.?)}");
      cSt.registerOutParameter(1, Types.BOOLEAN);
      cSt.setString(2,name);
      cSt.execute();
      return cSt.getBoolean(1);
    } catch (SQLException e) {
      return false;
    }
  }

  public boolean searchForProductFromCode(int code){
    try {
      cSt = connection.prepareCall("{? = CALL searchProductFromCode(?.?)}");
      cSt.registerOutParameter(1, Types.BOOLEAN);
      cSt.setInt(2,code);
      cSt.execute();
      return cSt.getBoolean(1);
    } catch (SQLException e) {
      return false;
    }
  }

  public void sellProduct(int code, int amount){
    try {
      cSt = connection.prepareCall("{CALL sellProduct(?.?)}");
      cSt.setInt(1,code);
      cSt.setInt(2,amount);
    } catch (SQLException e) {

    }
  }
}
