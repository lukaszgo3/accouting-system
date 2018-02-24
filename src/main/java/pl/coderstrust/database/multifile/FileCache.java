package pl.coderstrust.database.multifile;

import pl.coderstrust.database.ObjectMapperHelper;
import pl.coderstrust.model.Invoice;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FileCache {

  private FileHelper fileHelper;
  private ObjectMapperHelper objectMapper;
  public HashMap cashe;

  public FileCache() {
    fileHelper = new FileHelper();
    objectMapper = new ObjectMapperHelper();
    cashe = invoicesCache();
  }

  public HashMap invoicesCache() {
    List<File> files = fileHelper.listFiles("database");
    HashMap idCache = new HashMap();
    Invoice invoice;
    List<Invoice> invoices = new ArrayList<>();
    String line = null;
    for (int i = 0; i < files.size(); i++) {
      try {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(files.get(i)));
        while ((line = bufferedReader.readLine()) != null) {
          invoice = jsonToInvoice(line);
          idCache.put(invoice.getId(), files.get(i));
        }

      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return idCache;
  }

  Invoice jsonToInvoice(String json) {
    Invoice invoice = null;
    invoice = objectMapper.toInvoice(json);
    return invoice;
  }
}