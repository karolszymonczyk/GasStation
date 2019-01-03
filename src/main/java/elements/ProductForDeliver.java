package elements;

public class ProductForDeliver {

  String name;
  String code;
  int amount;

  public ProductForDeliver(String name, String code, int amount) {
    this.name = name;
    this.code = code;
    this.amount = amount;
  }

  public String getName() {
    return name;
  }

  public String getCode() {
    return code;
  }

  public int getAmount() {
    return amount;
  }
}
