package elements;

public class ManagerBill {

  String product;
  int amount;
  double price;

  public ManagerBill(String product, int amount, double price) {
    this.product = product;
    this.amount = amount;
    this.price = price;
  }

  public String getProduct() {
    return product;
  }

  public int getAmount() {
    return amount;
  }

  public double getPrice() {
    return price;
  }
}
