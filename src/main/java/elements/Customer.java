package elements;

public class Customer {

  int id;
  String name;
  String surname;
  int NIP;
  String created;

  public Customer(int id, String name, String surname, int NIP, String created) {
    this.id = id;
    this.name = name;
    this.surname = surname;
    this.NIP = NIP;
    this.created = created;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getSurname() {
    return surname;
  }

  public int getNIP() {
    return NIP;
  }

  public String getCreated() {
    return created;
  }
}
