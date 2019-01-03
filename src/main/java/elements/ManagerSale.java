package elements;

import java.util.ArrayList;

public class ManagerSale {

  int id;
  String date;
  String seller;
  double value;
  ArrayList<ManagerBill> elements;

  public ManagerSale(int id, String date, String seller, double value, ArrayList<ManagerBill> elements) {
    this.id = id;
    this.date = date;
    this.seller = seller;
    this.value = value;
    this.elements = elements;
  }

  public int getId() {
    return id;
  }

  public String getDate() {
    return date;
  }

  public String getSeller() {
    return seller;
  }

  public double getValue() {
    return value;
  }

  public ArrayList<ManagerBill> getElements() {
    return elements;
  }
}
