package pl.coderstrust.database.multifile;

import org.apache.commons.io.FileUtils;
import pl.coderstrust.database.Database;
import pl.coderstrust.database.DatabaseTest;
import pl.coderstrust.database.ObjectMapperHelper;

import java.io.File;
import java.io.IOException;

public class MultiFileDatabaseTest extends DatabaseTest {

  @Override
  public Database getCleanDatabase() {
    try {
      FileUtils.cleanDirectory(new File(Configuration.getJsonFilePath()));
    } catch (IOException e) {
      e.printStackTrace();
    }
    PathSelector pathSelector = new PathSelector();
    ObjectMapperHelper objectMapperHelper = new ObjectMapperHelper();
    FileCache fileCache = new FileCache(objectMapperHelper);
    fileCache.getCache().clear();
    return new MultiFileDatabase(objectMapperHelper,
        new FileHelper(fileCache, pathSelector),
        fileCache,
        pathSelector);
  }
}