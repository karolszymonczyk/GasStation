package dbConnection;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

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


  public boolean restoreBackup(String username, String password) {

    JFileChooser chooser = new JFileChooser("C:\\Users\\Szymon\\Documents\\dumps\\");

    if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
      File selectedFile = chooser.getSelectedFile();

      try {
        String[] executeCmd = new String[]{"mysql", dbName, "-u" + username, "-p" + password, "-e", " source " + selectedFile.getPath()};

        Process runtimeProcess = Runtime.getRuntime().exec(executeCmd);
        int processComplete = runtimeProcess.waitFor();

        return processComplete == 0;
      } catch (IOException | InterruptedException | HeadlessException ex) {
        return false;
      }
    }
    else{
      return false;
    }

  }
}

