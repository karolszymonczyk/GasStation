package elements;

public class ProductView {

  String code;
  String name;
  double price;
  double tax;
  int amount;

  public ProductView(String code, String name, double price, double tax, int amount) {
    this.code = code;
    this.name = name;
    this.price = price;
    this.tax = tax;
    this.amount = amount;
  }

  public String getCode() {
    return code;
  }

  public String getName() {
    return name;
  }

  public double getPrice() {
    return price;
  }

  public double getTax() {
    return tax;
  }

  public int getAmount() {
    return amount;
  }
}
