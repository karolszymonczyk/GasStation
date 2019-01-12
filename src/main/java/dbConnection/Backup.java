package dbConnection;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

//public class Backup {
//
//  public void executeBackUp() {
//
//    String username = "root";
//    String password = "root";
//    String executeCmd;
//
//    executeCmd = "\"C:\\Program Files\\MySQL\\MySQL Server 8.0\\bin\\mysqldump.exe\" -u root -proot stacjaPaliw > C:\\Users\\Szymon\\Documents\\dumps\\stacja.sql";
//
//    Process runtimeProcess = null;
//    int processComplete = 0;
//    try {
//      runtimeProcess = Runtime.getRuntime().exec(executeCmd);
//      processComplete = runtimeProcess.waitFor();
//    } catch (IOException e) {
//      e.printStackTrace();
//    }catch (InterruptedException e) {
//      e.printStackTrace();
//    }
//
//    if (processComplete == 0) {
//
//      System.out.println("Backup taken successfully");
//
//    } else {
//
//      System.out.println("processComplete = " + processComplete);
//      System.out.println("Could not take mysql backup");
//
//    }
//  }
//  public void restore() {
//    try {
//      String path = "filepath";
//      String arrayCommand[] = new String[]{"mysql", "databasename", "-u", "root", "-p" + "password", "-e", " source " + path};
//      Process runtimeProcess = Runtime.getRuntime().exec(arrayCommand);
//      int processStatus = runtimeProcess.waitFor();
//      if (processStatus == 1) {
//        System.out.println("failed");
//      } else if (processStatus == 0) {
//        System.out.println("success");
//      }
//    } catch (Exception e) {
//      e.printStackTrace();
//
//    }
//  }
//
//}

public class Backup {

  private static final String dbName = "stacjaPaliw";

  public boolean executeBackup(String path, String username, String password) {
    try {
      String executeCmd = "C:\\Program Files\\MySQL\\MySQL Server 8.0\\bin\\mysqldump.exe -u" + username + " -p" + password + " " + dbName + " -r " + path;

      Process runtimeProcess = Runtime.getRuntime().exec(executeCmd);
      int processComplete = runtimeProcess.waitFor();

      return processComplete == 0;

    } catch (IOException | InterruptedException ex) {
      ex.printStackTrace();
      return false;
    }
  }


  public boolean restoreBackup(String filePathWithBackup, String username, String password) {
    try {
      String[] executeCmd = new String[]{"mysql", dbName, "-u" + username, "-p" + password, "-e", " source " + filePathWithBackup};

      Process runtimeProcess = Runtime.getRuntime().exec(executeCmd);
      int processComplete = runtimeProcess.waitFor();

      return processComplete == 0;
    } catch (IOException | InterruptedException | HeadlessException ex) {
      return false;
    }
  }
}

