package elements;

public class BillElement {

  String product;

  double price;
  int quantity;
  double sum;
  int saleID;

  public BillElement() {

  }

  public BillElement(String product, int quantity, double price) {
    this.product = product;
    this.quantity = quantity;
    this.price = price;
    calculateSum();
  }

  private void calculateSum() {
    sum = Math.round(quantity * price*100);
    sum = sum/100;
  }

  public String getProduct() {
    return product;
  }

  public double getPrice() {
    return price;
  }

  public int getQuantity() {
    return quantity;
  }

  public double getSum() {
    return sum;
  }

  public void setProduct(String product) {
    this.product = product;
  }

  public void setPrice(double Price) {
    this.price = price;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public void setSum(double sum) {
    this.sum = sum;
  }

  public int getSaleID() {
    return saleID;
  }
}
