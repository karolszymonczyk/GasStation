package elements;

import java.util.ArrayList;
//
public class Deliver {

  int id;
  String date;
  String deliverer;
  String storekeeper;
  ArrayList<ProductForDeliver> elements;

  public Deliver(int id, String date, String deliverer, String storekeeper, ArrayList<ProductForDeliver> elements) {
    this.id = id;
    this.date = date;
    this. deliverer = deliverer;
    this.storekeeper = storekeeper;
    this.elements = elements;
  }

  public int getId() {
    return id;
  }

  public String getDate() {
    return date;
  }

  public String getDeliverer() {
    return deliverer;
  }

  public String getStorekeeper() {
    return storekeeper;
  }

  public ArrayList<ProductForDeliver> getElements() {
    return elements;
  }
}
