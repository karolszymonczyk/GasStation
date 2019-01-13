package dbConnection;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class Backup {

  private static final String dbName = "StacjaPaliw";

  public boolean executeBackup(String path, String username, String password) {
    try {
      String executeCmd = "\"C:\\Program Files\\MySQL\\MySQL Server 8.0\\bin\\mysqldump.exe\" -u" + username + " -p" + password + " " + dbName + " -r " + "\"" + path + "\"";

      System.out.println(executeCmd);

      Process runtimeProcess = Runtime.getRuntime().exec(executeCmd);
      int processComplete = runtimeProcess.waitFor();

      return processComplete == 0;

    } catch (IOException | InterruptedException ex) {
      ex.printStackTrace();
      return false;
    }
  }


  public boolean restoreBackup(String username, String password) {

    JFileChooser chooser = null;
    try {
      chooser = new JFileChooser(this.getClass().getResource("/backup").toURI().toString());
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }

    if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
      File selectedFile = chooser.getSelectedFile();

      try {
        System.out.println(selectedFile.getPath());
//        String[] executeCmd = new String[]{"C:\\Program Files\\MySQL\\MySQL Server 8.0\\bin mysql", dbName, "-u" + username, "-p" + password, "-e", " source " + selectedFile.getPath()};

//        String command = "\"" + "C:\\Program Files\\MySQL\\MySQL Server 8.0\\bin\\mysql\" " + "mysql -uroot -p19Wrzesie StacjaPaliw < " + "\"" + selectedFile.getPath() + "\"";
        String command = "mysql --user=root --password=19Wrzesien -e source " + "\"" + selectedFile.getPath() + "\"";
//        String command = "\"C:\\Program Files\\MySQL\\MySQL Shell 8.0\\bin\\mysqlsh.exe\" -uroot -p19Wrzesien StacjaPaliw < " + "\"" + selectedFile.getPath() + "\"";

        System.out.println(command);

        Process runtimeProcess = Runtime.getRuntime().exec(command);
        int processComplete = runtimeProcess.waitFor();

        System.out.println("siema");
        System.out.println(processComplete);

        return processComplete == 0;
      } catch (IOException | InterruptedException | HeadlessException ex) {
        ex.printStackTrace();
        return false;
      }
    }
    else{
      return false;
    }

  }
}

