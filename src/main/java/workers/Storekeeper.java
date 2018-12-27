package workers;

import java.sql.*;

public class Storekeeper extends Worker{


  public Storekeeper(Connection connection){
    this.connection = connection;
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
      return false;
    }
    return true;
  }
}
