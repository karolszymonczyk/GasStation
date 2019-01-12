package elements;

import java.time.LocalDate;

public class Log {

  int id;
  String name;
  String surname;
  String operation;
  String time;

  public Log(int id, String name, String surname, String operation, String time) {
    this.id = id;
    this.name = name;
    this.surname = surname;
    this.operation = operation;
    this.time = time;
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

  public String getOperation() {
    return operation;
  }

  public String getTime() {
    return time;
  }
}
