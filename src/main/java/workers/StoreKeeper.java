package workers;

import java.sql.*;

public class StoreKeeper extends Worker{


  public StoreKeeper(Connection connection){
    this.connection = connection;
  }

  public boolean addExistingProduct(int code, int amount){
    try {
      cSt = connection.prepareCall("{CALL insertExistingProduct(?.?)}");
      cSt.setInt(1,code);
      cSt.setInt(2,amount);
    } catch (SQLException e) {
      return false;
    }
    return true;
  }

  public boolean addNewProduct(int code, String name, float price, float tax, int amount){
    try {
      cSt = connection.prepareCall("{CALL insertExistingProduct(?,?.?,?,?)}");
      cSt.setInt(1,code);
      cSt.setString(2,name);
      cSt.setFloat(3,price);
      cSt.setFloat(4,tax);
      cSt.setInt(5,amount);
    } catch (SQLException e) {
      return false;
    }
    return true;
  }
}
