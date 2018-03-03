package pl.coderstrust.database.multifile;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderstrust.database.DbException;
import pl.coderstrust.database.ExceptionMsg;
import pl.coderstrust.database.ObjectMapperHelper;
import pl.coderstrust.model.Invoice;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class FileCache {

  private ObjectMapperHelper objectMapper;
  private HashMap<Long, String> cache;
  private FileCache instance;

  @Autowired
  public FileCache(ObjectMapperHelper objectMapper) {
    this.objectMapper = objectMapper;
    cache = getActualFileCache();
  }

  private HashMap getActualFileCache() {
    ArrayList<String> allFiles = getAllFilesEntries();
    HashMap<Long, String> tempCache = new HashMap<>();
    for (String json : allFiles) {
      Invoice invoice = jsonToInvoice(json);
      tempCache.put(invoice.getId(), new PathSelector().getFilePath(invoice));
    }
    return tempCache;
  }

  public ArrayList<String> getAllFilesEntries() {
    ArrayList<String> readFiles = new ArrayList<>();
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

  private Invoice jsonToInvoice(String json) {
    return objectMapper.toInvoice(json);
  }

  public HashMap getCache() {
    return cache;
  }
}