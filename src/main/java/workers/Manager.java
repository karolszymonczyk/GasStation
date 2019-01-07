package workers;

import elements.*;

import java.sql.*;
import java.util.ArrayList;

public class Manager extends Worker {

  ArrayList<ManagerSale> bills;
  ArrayList<ManagerBill> billElements;
  ManagerSale managerSale;
  ManagerBill managerBill;
  boolean transactionStarted = false;

  boolean tranactionStarted = false;
  private ArrayList<ProductView> products;
  private ArrayList<Product> productsToSale;
  ArrayList<elements.Worker> workers;
  ArrayList<Deliver> deliveries;
  ArrayList<ProductForDeliver> deliveryElements;
  ProductForDeliver productForDeliver;
  private Deliver delivery;

  public Manager(Connection connection) {
    this.connection = connection;
    downloadAll();
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
        String time = rs.getTimestamp("time").toString();
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

  public void downloadDeliveries(){
    deliveries = new ArrayList<>();

    try {
      st = connection.createStatement();
      rs = st.executeQuery("SELECT d.id,d.time,de.product_id,de.amount,(SELECT name FROM product where code = de.product_id) as name,(SELECT CONCAT(name,' ',surname) FROM worker WHERE id = d.storekeeper) as storekeeper, de.deliverer FROM delivery d JOIN delivery_deliveryelement dde ON dde.delivery = d.id JOIN delivery_element de ON dde.delivery_element = de.id;");
      int id1;
      int id2=-1;
      while (rs.next()) {

        id1 = rs.getInt("id");
        String time = rs.getTimestamp("time").toString();
        String storekeeper = rs.getString("storekeeper");
        String deliverer = rs.getString("deliverer");
        String name = rs.getString("name");
        int amount = rs.getInt("amount");
        Integer code = rs.getInt("product_id");

        productForDeliver = new ProductForDeliver(name, code.toString(), amount);


        if(id1==id2){
          deliveries.remove(deliveries.size()-1);
        }

        else{
          deliveryElements = new ArrayList<>();
        }

        deliveryElements.add(productForDeliver);
        delivery = new Deliver(id1,time,deliverer,storekeeper,deliveryElements);
        deliveries.add(delivery);
        id2=id1;

      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public ArrayList<ManagerSale> getBills() {
    return bills;
  }

  public boolean isTranactionStarted() {
    return tranactionStarted;
  }

  public void setTranactionStarted(boolean tranactionStarted) {
    this.tranactionStarted = tranactionStarted;
  }

  public void downloadProducts(){
    products = new ArrayList<>();
    productsToSale = new ArrayList<>();

    try {
      st = connection.createStatement();
      rs = st.executeQuery("SELECT * FROM product");
      while (rs.next()) {


        Integer code = rs.getInt("code");
        String name = rs.getString("name");
        float price = rs.getFloat("price");
        double tax = rs.getFloat("tax");
        int amount = rs.getInt("amount");

        ProductView productView = new ProductView(code.toString(),name,price,tax,amount);
        Product product = new Product(name,code.toString());
        productsToSale.add(product);
        products.add(productView);
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }


  public void downloadWorkers(){
    workers = new ArrayList<>();

    try {
      st = connection.createStatement();
      rs = st.executeQuery("SELECT * FROM worker w JOIN users u ON w.id=u.worker_id");
      while (rs.next()) {

        int id = rs.getInt("id");
        String name = rs.getString("name");
        String surname = rs.getString("surname");
        String login = rs.getString("login");
        String password = rs.getString("password");
        String contractS = rs.getDate("contract_start").toString();
        String contractE = rs.getDate("contract_end").toString();
        String job = rs.getString("job");
        String status = rs.getString("status");

        elements.Worker worker = new elements.Worker(id,name,surname,login,password,contractS,contractE,job,status) {
        };
        workers.add(worker);
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public ArrayList<ProductView> getProducts() {
    return products;
  }

  public ArrayList<elements.Worker> getWorkers() {
    return workers;
  }

  public void addProduct(String name, float price, float tax, int code, int amount) {
    try {
      st = connection.createStatement();
      String sql = "INSERT INTO product (name, price, tax, code, amount) VALUES ('"+name+"',"+price+","+tax+","+code+","+amount+")";
      st.executeUpdate(sql);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void deleteProduct(int code){
    try {
      st = connection.createStatement();
      String sql = "DELETE FROM product WHERE code = " + code;
      st.executeUpdate(sql);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void deleteSale(int id){
    try {
      st = connection.createStatement();
      String sql = "DELETE FROM bill WHERE id = " + id;
      st.executeUpdate(sql);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void deleteDelivery(int id){
    try {
      st = connection.createStatement();
      String sql = "DELETE FROM delivery WHERE id = " + id;
      st.executeUpdate(sql);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void downloadAll(){
    downloadWorkers();
    downloadProducts();
    downloadBills();
    downloadDeliveries();
  }

  public ArrayList<Product> getProductsToSale() {
    return productsToSale;
  }

  public boolean isTransactionStarted() {
    return transactionStarted;
  }

  public void setTransactionStarted(boolean transactionStarted) {
    this.transactionStarted = transactionStarted;
  }

  public ArrayList<Deliver> getDeliveries() {
    return deliveries;
  }

  public void deleteUser(int id){
    try {

      cSt = connection.prepareCall("{CALL deleteUser(?)}");
      cSt.setInt(1, id);
      cSt.execute();

      st = connection.createStatement();
      String sql = "DELETE FROM worker WHERE id = " + id;
      st.executeUpdate(sql);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void createUser(String name, String surname, String job, Date start, Date end) {


    try{
      cSt = connection.prepareCall("{CALL createWorker(?,?,?,?,?)}");
      cSt.setString(1,name);
      cSt.setString(2,surname);
      cSt.setString(3,job);
      cSt.setDate(4,start);
      cSt.setDate(5,end);
      cSt.execute();

      st = connection.createStatement();
      rs = st.executeQuery("SELECT id FROM worker WHERE name = '"+name+"' && surname = '" + surname + "'");
      int id = 0;
      while (rs.next()) {

        id = rs.getInt("id");

      }
      cSt = connection.prepareCall("CALL createUser(?)");
      cSt.setInt(1,id);
      cSt.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
