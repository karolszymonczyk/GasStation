package workers;

import elements.ManagerBill;
import elements.ManagerSale;
import elements.Product;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class Manager extends Worker {

  ArrayList<ManagerSale> bills;
  ArrayList<ManagerBill> billElements;
  ArrayList<ManagerBill> billElements2;
  ManagerSale managerSale;
  ManagerBill managerBill;

  public Manager(Connection connection) {
    this.connection = connection;
    downloadBills();
  }

  public void downloadBills(){
    bills = new ArrayList<>();

    try {
      st = connection.createStatement();
      rs = st.executeQuery("SELECT b.id,b.time,(SELECT CONCAT(name,' ',surname) FROM worker WHERE id = b.worker_id) as worker," +
                                "b.value," +
                                "(SELECT name FROM customer WHERE id = b.customer) as customer," +
                                "(SELECT price FROM product WHERE code = s.product_id) as price, " +
                                "(SELECT name from product p WHERE p.code = s.product_id) as product,s.amount " +
                                "FROM bill b JOIN bill_sale ON b.id = bill_sale.bill_id JOIN sale s ON bill_sale.sale_id = s.id;");


      int id1;
      int id2=-1;
      while (rs.next()) {


        id1 = rs.getInt("id");
        String time = rs.getDate("time").toString();
        String seller = rs.getString("worker");
        double value = rs.getFloat("value");
        String product = rs.getString("product");
        int amount = rs.getInt("amount");
        float price = rs.getFloat("price");

        managerBill = new ManagerBill(product,amount,price);


        if(id1==id2){
          bills.remove(bills.size()-1);
        }

        else{
          billElements = new ArrayList<>();
        }

        billElements.add(managerBill);
        managerSale = new ManagerSale(id1,time,seller,value, billElements);
        bills.add(managerSale);
        id2=id1;

      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public ArrayList<ManagerSale> getBills() {
    return bills;
  }
}
