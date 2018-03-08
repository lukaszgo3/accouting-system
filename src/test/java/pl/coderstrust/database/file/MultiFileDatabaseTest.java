package pl.coderstrust.database.file;

import org.apache.commons.io.FileUtils;
import pl.coderstrust.database.Database;
import pl.coderstrust.database.DatabaseTest;
import pl.coderstrust.database.ObjectMapperHelper;
import pl.coderstrust.database.file.multifile.Configuration;
import pl.coderstrust.database.file.multifile.FileCache;
import pl.coderstrust.database.file.multifile.MultiFileDatabase;
import pl.coderstrust.database.file.multifile.PathSelector;
import pl.coderstrust.model.Invoice;

import java.io.File;
import java.io.IOException;

public class MultiFileDatabaseTest extends DatabaseTest {

  @Override
  public Database getCleanDatabase() {
    try {
      FileUtils.forceMkdir(new File(Configuration.getJsonFilePath()));
    } catch (IOException e) {
      e.printStackTrace();
    }
    try {
      FileUtils.cleanDirectory(new File(Configuration.getJsonFilePath()));
    } catch (IOException e) {
      e.printStackTrace();
    }
    PathSelector pathSelector = new PathSelector();
    ObjectMapperHelper objectMapperHelper = new ObjectMapperHelper(Invoice.class);
    FileCache fileCache = new FileCache(objectMapperHelper);
    fileCache.getCache().clear();

    Database database = new MultiFileDatabase<Invoice>(Invoice.class);
    return database;
  }
}