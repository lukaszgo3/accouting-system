package pl.coderstrust.database.multifile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import pl.coderstrust.database.Database;
import pl.coderstrust.model.Invoice;

import java.util.List;

public class MultiFileDatabase implements Database {

  private ObjectMapper objectMapper = new ObjectMapper();
  private PathSelector pathSelector;
  FileHelper fileHelper = new FileHelper();

  @Override
  public void addInvoice(Invoice invoice) {
    try {
      fileHelper.addLine(objectMapper.writeValueAsString(invoice), invoice);
    } catch (JsonProcessingException e) {
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
