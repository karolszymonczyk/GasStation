package workers;

import elements.*;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

public class Manager extends Worker {


  private ManagerSale managerSale;
  private ManagerBill managerBill;
  private ProductForDeliver productForDeliver;
  private Deliver delivery;

  private ArrayList<ProductView> products;
  private ArrayList<Product> productsToSale;
  private ArrayList<elements.Worker> workers;
  private ArrayList<Deliver> deliveries;
  private ArrayList<ProductForDeliver> deliveryElements;
  private ArrayList<ManagerSale> bills;
  private ArrayList<ManagerBill> billElements;
  private ArrayList<ProductForDeliver> deliveredProducts;
  private ArrayList<Customer> customers;

  private boolean transactionStarted;

  public Manager(Connection connection) {
    this.connection = connection;
    deliveredProducts = new ArrayList<>();
    downloadAll();
  }

  public void downloadBills(int option) {
    bills = new ArrayList<>();

    try {

        st = connection.createStatement();

      if(option == 1){
        rs = st.executeQuery("SELECT b.id,b.time,(SELECT CONCAT(name,' ',surname) FROM worker WHERE id = b.worker_id) as worker," +
                "b.value," +
                "(SELECT name FROM customer WHERE id = b.customer) as customer," +
                "(SELECT price FROM product WHERE code = s.product_id) as price, " +
                "(SELECT name from product p WHERE p.code = s.product_id) as product,s.amount " +
                "FROM bill b JOIN bill_sale ON b.id = bill_sale.bill_id JOIN sale s ON bill_sale.sale_id = s.id WHERE MONTH(b.time) = MONTH(current_date) AND YEAR(b.time) = YEAR(current_date);");

      } else if(option == 2){
        rs = st.executeQuery("SELECT b.id,b.time,(SELECT CONCAT(name,' ',surname) FROM worker WHERE id = b.worker_id) as worker," +
                "b.value," +
                "(SELECT name FROM customer WHERE id = b.customer) as customer," +
                "(SELECT price FROM product WHERE code = s.product_id) as price, " +
                "(SELECT name from product p WHERE p.code = s.product_id) as product,s.amount " +
                "FROM bill b JOIN bill_sale ON b.id = bill_sale.bill_id JOIN sale s ON bill_sale.sale_id = s.id WHERE (MONTH(b.time) = MONTH(current_date) OR MONTH(b.time) = MONTH(current_date - INTERVAL 1 MONTH)) AND YEAR(b.time) = YEAR(current_date);");

      } else if(option == 3){
        rs = st.executeQuery("SELECT b.id,b.time,(SELECT CONCAT(name,' ',surname) FROM worker WHERE id = b.worker_id) as worker," +
                "b.value," +
                "(SELECT name FROM customer WHERE id = b.customer) as customer," +
                "(SELECT price FROM product WHERE code = s.product_id) as price, " +
                "(SELECT name from product p WHERE p.code = s.product_id) as product,s.amount " +
                "FROM bill b JOIN bill_sale ON b.id = bill_sale.bill_id JOIN sale s ON bill_sale.sale_id = s.id WHERE YEAR(b.time) = YEAR(current_date);");

      } else if(option == 4){


          rs = st.executeQuery("SELECT b.id,b.time,(SELECT CONCAT(name,' ',surname) FROM worker WHERE id = b.worker_id) as worker," +
                  "b.value," +
                  "(SELECT name FROM customer WHERE id = b.customer) as customer," +
                  "(SELECT price FROM product WHERE code = s.product_id) as price, " +
                  "(SELECT name from product p WHERE p.code = s.product_id) as product,s.amount " +
                  "FROM bill b JOIN bill_sale ON b.id = bill_sale.bill_id JOIN sale s ON bill_sale.sale_id = s.id");
          }

      int id1;
      int id2 = -1;
      while (rs.next()) {

        id1 = rs.getInt("id");
        String time = rs.getTimestamp("time").toString();
        String seller = rs.getString("worker");
        double value = rs.getFloat("value");
        String product = rs.getString("product");
        int amount = rs.getInt("amount");
        double price = rs.getFloat("price");
        price = round(price);
        value = round(value);

        managerBill = new ManagerBill(product, amount, price);

        if (id1 == id2) {
          bills.remove(bills.size() - 1);
        } else {
          billElements = new ArrayList<>();
        }

        billElements.add(managerBill);
        managerSale = new ManagerSale(id1, time, seller, value, billElements);
        bills.add(managerSale);
        id2 = id1;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void downloadCustomers(){
    customers = new ArrayList<>();

    try {
      st = connection.createStatement();
      rs = st.executeQuery("SELECT * FROM customer");
      while (rs.next()) {

        Integer id = rs.getInt("id");
        Integer NIP = rs.getInt("NIP");
        String name = rs.getString("name");
        String surname = rs.getString("surname");
        String date = rs.getDate("created").toString();

        Customer customer = new Customer (id,name,surname,NIP,date);
        customers.add(customer);

      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void downloadDeliveries() {
    deliveries = new ArrayList<>();

    try {
      st = connection.createStatement();
      rs = st.executeQuery("SELECT d.id,d.time,de.product_id,de.amount,(SELECT name FROM product where code = de.product_id) as name,(SELECT CONCAT(name,' ',surname) FROM worker WHERE id = d.storekeeper) as storekeeper, d.deliverer FROM delivery d JOIN delivery_deliveryelement dde ON dde.delivery = d.id JOIN delivery_element de ON dde.delivery_element = de.id;");
      int id1;
      int id2 = -1;
      while (rs.next()) {

        id1 = rs.getInt("id");
        String time = rs.getTimestamp("time").toString();
        String storekeeper = rs.getString("storekeeper");
        String deliverer = rs.getString("deliverer");
        String name = rs.getString("name");
        int amount = rs.getInt("amount");
        int code = rs.getInt("product_id");

        productForDeliver = new ProductForDeliver(name, Integer.toString(code), amount);

        if (id1 == id2) {
          deliveries.remove(deliveries.size() - 1);
        } else {
          deliveryElements = new ArrayList<>();
        }
        deliveryElements.add(productForDeliver);
        delivery = new Deliver(id1, time, deliverer, storekeeper, deliveryElements);
        deliveries.add(delivery);
        id2 = id1;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public ArrayList<ManagerSale> getBills() {
    return bills;
  }

  public void downloadProducts() {
    products = new ArrayList<>();
    productsToSale = new ArrayList<>();

    try {
      st = connection.createStatement();
      rs = st.executeQuery("SELECT * FROM product");
      while (rs.next()) {

        Integer code = rs.getInt("code");
        String name = rs.getString("name");
        double price = rs.getFloat("price");
        double tax = rs.getFloat("tax");
        int amount = rs.getInt("amount");
        price = round(price);
        tax = round(tax);

        ProductView productView = new ProductView(code.toString(), name, price, tax, amount);
        Product product = new Product(name, code.toString());
        productsToSale.add(product);
        products.add(productView);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }


  public void downloadWorkers() {
    workers = new ArrayList<>();

    try {
      st = connection.createStatement();
      rs = st.executeQuery("SELECT * FROM worker w JOIN users u ON w.id=u.worker_id");
      while (rs.next()) {

        int id = rs.getInt("id");
        String name = rs.getString("name");
        String surname = rs.getString("surname");
        String login = rs.getString("login");
//        String password = rs.getString("password");
        String contractS = rs.getDate("contract_start").toString();
        String contractE = rs.getDate("contract_end").toString();
        String job = rs.getString("job");

        elements.Worker worker = new elements.Worker(id, name, surname, login, contractS, contractE, job);
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
      String sql = "INSERT INTO product (name, price, tax, code, amount) VALUES ('" + name.toLowerCase() + "'," + price + "," + tax + "," + code + "," + amount + ")";
      st.executeUpdate(sql);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void deleteProduct(int code) {
    try {
      st = connection.createStatement();
      String sql = "DELETE FROM product WHERE code = " + code;
      st.executeUpdate(sql);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void deleteSale(int id) {
    try {
      st = connection.createStatement();
      String sql = "DELETE FROM bill WHERE id = " + id;
      st.executeUpdate(sql);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void deleteDelivery(int id) {
    try {
      st = connection.createStatement();
      String sql = "DELETE FROM delivery WHERE id = " + id;
      st.executeUpdate(sql);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void downloadAll() {
    downloadWorkers();
    downloadProducts();
    downloadBills(1);
    downloadDeliveries();
    downloadCustomers();
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

  public void deleteUser(int id) {
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

  public void  deleteCustomer(int id) {
    try {

      cSt = connection.prepareCall("{CALL deleteCustomer(?)}");
      cSt.setInt(1, id);
      cSt.execute();

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
  public void createUser(String name, String surname, String job, Date start, Date end) throws SQLException {



      cSt = connection.prepareCall("{CALL createWorker(?,?,?,?,?)}");
      cSt.setString(1, name);
      cSt.setString(2, surname);
      cSt.setString(3, job.toLowerCase());
      cSt.setDate(4, start);
      cSt.setDate(5, end);
      cSt.execute();

      st = connection.createStatement();
      rs = st.executeQuery("SELECT id FROM worker WHERE name = '" + name + "' && surname = '" + surname + "'");
      int id = 0;
      while (rs.next()) {

        id = rs.getInt("id");

      }
      cSt = connection.prepareCall("CALL createUser(?)");
      cSt.setInt(1, id);
      cSt.execute();
    }


  public void addDeliveryProduct(ProductForDeliver product) {
    deliveredProducts.add(product);
  }

  public ArrayList<ProductForDeliver> getDeliveredProducts() {
    return deliveredProducts;
  }

  public ArrayList<Customer> getCustomers() {
    return customers;
  }
}
