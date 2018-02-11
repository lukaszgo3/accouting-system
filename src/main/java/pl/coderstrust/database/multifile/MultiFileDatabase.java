package pl.coderstrust.database.multifile;

import com.fasterxml.jackson.databind.ObjectMapper;

import pl.coderstrust.database.Database;
import pl.coderstrust.model.Invoice;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MultiFileDatabase implements Database {

  ObjectMapper objectMapper = new ObjectMapper();
  PathSelector pathSelector;
  Invoice invoice;
  String filePath;

  @Override
  public void addInvoice(Invoice invoice) {
    try {
      objectMapper.writeValue(new File(pathSelector.getFilePath(invoice)), invoice);
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
