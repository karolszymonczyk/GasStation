package dbConnection;

import java.io.IOException;

public class Backup {

  public void executeBackUp() {

    String username = "root";
    String password = "password";
    String filePath = "./src/backup";


    String executeCmd = "";

    executeCmd = "C:\\Program Files\\MySQL\\MySQL Server 8.0\\bin\\mysqldump -u root -proot stacjaPaliw > C:\\Users\\Szymon\\Documents\\dumps\\stacja1";

    Process runtimeProcess = null;
    int processComplete = 0;
    try {
      System.out.println("1111111111111");
      runtimeProcess = Runtime.getRuntime().exec(executeCmd);
      System.out.println("22222222222222");
      processComplete = runtimeProcess.waitFor();
      System.out.println("3333333333333");
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
