package pl.coderstrust.database.database.memory;

import java.util.List;
import pl.coderstrust.database.Database;
import pl.coderstrust.model.Invoice;

public class InMemoryDatabase implements Database {

  List<Invoice> invoices;

  @Override
  public long addInvoice(Invoice invoice) {
    return 0;
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
