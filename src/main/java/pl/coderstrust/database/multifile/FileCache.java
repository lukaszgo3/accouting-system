package pl.coderstrust.database.multifile;

import pl.coderstrust.database.ObjectMapperHelper;
import pl.coderstrust.model.Invoice;

import java.util.ArrayList;
import java.util.HashMap;

public class FileCache {

  private FileHelper fileHelper;
  private ObjectMapperHelper objectMapper;
  private HashMap cashe;

  public FileCache() {
    fileHelper = new FileHelper();
    objectMapper = new ObjectMapperHelper();
    cashe = invoicesCache();
  }

  public HashMap getCashe() {
    return cashe;
  }

  public HashMap invoicesCache() {
    ArrayList<String> allFiles;
    allFiles = fileHelper.getAllFilesEntries();
    HashMap idCache = new HashMap();
    Invoice invoice;
    for (String json : allFiles) {
      invoice = jsonToInvoice(json);
      idCache.put(invoice.getId(), new PathSelector().getFilePath(invoice));
    }
    return idCache;
  }

  Invoice jsonToInvoice(String json) {
    Invoice invoice = null;
    invoice = objectMapper.toInvoice(json);
    return invoice;
  }
}