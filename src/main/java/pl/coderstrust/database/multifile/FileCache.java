package pl.coderstrust.database.multifile;

import pl.coderstrust.database.ObjectMapperHelper;
import pl.coderstrust.model.Invoice;

import java.util.ArrayList;
import java.util.HashMap;

public class FileCache {

  private FileHelper fileHelper;
  private ObjectMapperHelper objectMapper;
  private HashMap cache;
  private HashMap tempCache = new HashMap();

  public FileCache() {
    fileHelper = new FileHelper();
    objectMapper = new ObjectMapperHelper();
    cache = invoicesCache();
  }

  public HashMap getCache() {
    return cache;
  }

  public HashMap invoicesCache() {
    ArrayList<String> allFiles;
    allFiles = fileHelper.getAllFilesEntries();
    Invoice invoice;
    for (String json : allFiles) {
      invoice = jsonToInvoice(json);
      tempCache.put(invoice.getId(), new PathSelector().getFilePath(invoice));
    }
    return tempCache;
  }

  Invoice jsonToInvoice(String json) {
    Invoice invoice = null;
    invoice = objectMapper.toInvoice(json);
    return invoice;
  }
}