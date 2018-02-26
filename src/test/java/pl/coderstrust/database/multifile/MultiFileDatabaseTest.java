package pl.coderstrust.database.multifile;

import org.apache.commons.io.FileUtils;
import pl.coderstrust.database.Database;
import pl.coderstrust.database.DatabaseTest;

import java.io.File;
import java.util.List;

public class MultiFileDatabaseTest extends DatabaseTest {

  private static final int WAIT_TIME_FOR_FILESYSTEM = 4000;
  private static final int UNIT_WAIT_TIME_FOR_FILESYSTEM = 100;
  private Configuration config = new Configuration();

  @Override
  public Database getCleanDatabase() {
    String[] extensions = new String[]{"json"};
    List<File> files = (List<File>) FileUtils
        .listFiles(new File(config.getJsonFilePath()), extensions, true);
    if (files.size() > 0) {
      for (File f : files) {
        f.delete();
      }
    }
    return new MultiFileDatabase();
  }
}