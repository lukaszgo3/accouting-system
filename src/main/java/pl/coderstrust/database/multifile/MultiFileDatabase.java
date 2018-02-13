package pl.coderstrust.database.multifile;

import com.fasterxml.jackson.databind.ObjectMapper;

import pl.coderstrust.database.Database;
import pl.coderstrust.model.Invoice;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class MultiFileDatabase implements Database {

  private ObjectMapper objectMapper = new ObjectMapper();
  private PathSelector pathSelector;

  @Override
  public void addInvoice(Invoice invoice) {
    try {
      pathSelector = new PathSelector(invoice);
      File file = new File(pathSelector.getFilePath(invoice));
      file.getParentFile().mkdirs();
      objectMapper.writeValue(new FileWriter((file), true), invoice);
      System.out.println("Adding invoice:" + invoice.getSystemId());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void deleteInvoiceById(long id) {

  }

  @Override
  public Invoice getInvoiceById(long id) {

    return null;
  }

  @Override
  public void updateInvoice(Invoice invoice) {

  }

  @Override
  public List<Invoice> getInvoices() {
    return null;
  }
}
