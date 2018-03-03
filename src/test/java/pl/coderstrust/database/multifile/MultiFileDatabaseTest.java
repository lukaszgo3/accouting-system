package pl.coderstrust.database.multifile;

import org.apache.commons.io.FileUtils;
import pl.coderstrust.database.Database;
import pl.coderstrust.database.DatabaseTest;
import pl.coderstrust.database.ObjectMapperHelper;

import java.io.File;
import java.util.List;

public class MultiFileDatabaseTest extends DatabaseTest {

  @Override
  public Database getCleanDatabase() {
    String[] extensions = new String[]{"json"};
    List<File> files = (List<File>) FileUtils
        .listFiles(new File(Configuration.getJsonFilePath()), extensions, true);
    if (files.size() > 0) {
      for (File f : files) {
        f.delete();
      }
    }

    return new MultiFileDatabase(new ObjectMapperHelper(), new FileHelper(FileCache.getFileCache()),
        FileCache.getFileCache(),
        new PathSelector());
  }
}