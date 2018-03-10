package pl.coderstrust.database.multifile;

import org.apache.commons.io.FileUtils;
import pl.coderstrust.database.DbException;
import pl.coderstrust.database.ExceptionMsg;
import pl.coderstrust.database.ObjectMapperHelper;
import pl.coderstrust.model.HasNameIdIssueDate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FileCache<T extends HasNameIdIssueDate> {

  private ObjectMapperHelper objectMapper;
  private HashMap<Long, String> cache;

  public FileCache(ObjectMapperHelper objectMapper) {
    this.objectMapper = objectMapper;
    cache = getActualFileCache();
  }

  private HashMap getActualFileCache() {
    ArrayList<String> allFiles = getAllFilesEntries();
    HashMap<Long, String> tempCache = new HashMap<>();
    for (String json : allFiles) {
      T invoice = jsonToInvoice(json);
      tempCache.put(invoice.getId(), new PathSelector().getFilePath(invoice));
    }
    return tempCache;
  }

  public ArrayList<String> getAllFilesEntries() {
    ArrayList<String> readFiles = new ArrayList<>();
    if (!new File(Configuration.getJsonFilePath()).exists()) {
      return readFiles;
    }
    listFiles((Configuration.getJsonFilePath())).stream().map(File::toString).forEach(str -> {
      try (BufferedReader bufferedReader = new BufferedReader(new FileReader(str))) {
        String line;
        while ((line = bufferedReader.readLine()) != null) {
          readFiles.add(line);
        }
      } catch (IOException e) {
        throw new DbException(
            ExceptionMsg.IO_ERROR_WHILE_READING, e);
        //TODO add logging.
      }
    });
    return readFiles;
  }

  private List<File> listFiles(String directoryName) {
    File dir = new File(directoryName);
    String[] extensions = new String[]{"json"};
    return (List<File>) FileUtils.listFiles(dir, extensions, true);
  }

  private T jsonToInvoice(String json) {
    return (T) objectMapper.toObject(json);
  }

  public HashMap getCache() {
    return cache;
  }
}