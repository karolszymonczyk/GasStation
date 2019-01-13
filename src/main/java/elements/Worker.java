package elements;

public class Worker {

  int id;
  String name;
  String surname;
  String login;
  String startContract;
  String endContract;
  String job;

  public Worker(int id, String name, String surname, String login, String startContract, String endContract, String job) {

    this.id = id;
    this.name = name;
    this.surname = surname;
    this.login = login;
    this.startContract = startContract;
    this.endContract = endContract;
    this.job = job;
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

  public String getStartContract() {
    return startContract;
  }

  public String getEndContract() {
    return endContract;
  }

  public String getJob() {
    return job;
  }

  public Worker getWorker(){
    return this;
  }
}
