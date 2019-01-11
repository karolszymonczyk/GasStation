package dbConnection;

import java.io.IOException;

public class Backup {

  public void executeBackUp() {

    String username = "root";
    String password = "password";
    String filePath = "./src/backup";


    String executeCmd = "";
    executeCmd = "mysqldump - u "+username +"-p"+password +"+stacjapaliw@localhost -r >  backup.sql";

    Process runtimeProcess = null;
    int processComplete = 0;
    try {
      runtimeProcess = Runtime.getRuntime().exec(executeCmd);
      processComplete = runtimeProcess.waitFor();
    } catch (IOException e) {
      e.printStackTrace();
    }catch (InterruptedException e) {
      e.printStackTrace();
    }

    if (processComplete == 0) {

      System.out.println("Backup taken successfully");

    } else {

      System.out.println("Could not take mysql backup");

    }
  }
  public void restore() {
    try {
      String path = "filepath";
      String arrayCommand[] = new String[]{"mysql", "databasename", "-u", "root", "-p" + "password", "-e", " source " + path};
      Process runtimeProcess = Runtime.getRuntime().exec(arrayCommand);
      int processStatus = runtimeProcess.waitFor();
      if (processStatus == 1) {
        System.out.println("failed");
      } else if (processStatus == 0) {
        System.out.println("success");
      }
    } catch (Exception e) {
      e.printStackTrace();

    }
  }

}
