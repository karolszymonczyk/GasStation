package elements;

public class Worker {

  int id;
  String name;
  String surname;
  String login;
  String password;
  String startContract;
  String endContract;
  String job;
  String status;

  public Worker(int id, String name, String surname, String login, String password, String startContract, String endContract, String job, String status) {
    this.id = id;
    this.name = name;
    this.surname = surname;
    this.login = login;
    this.password = password;
    this.startContract = startContract;
    this.endContract = endContract;
    this.job = job;
    this.status = status;
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

  public String getLogin() {
    return login;
  }

  public String getPassword() {
    return password;
  }

  public String getStartContract() {
    return startContract;
  }

  public String getEndContract() {
    return endContract;
  }

  public String getJob() {
    return job;
  }

  public String getStatus() {
    return status;
  }
}
