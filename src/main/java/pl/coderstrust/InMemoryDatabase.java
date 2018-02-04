package pl.coderstrust;

import java.util.List;

public class InMemoryDatabase implements DataBase {

  List<Invoice> invoices;

  @Override
  public boolean addInvoice(Invoice invoice) {
    return false;
  }

  @Override
  public boolean deleteInvoiceById(Id id) {
    return false;
  }

  @Override
  public Invoice getInvoiceById(Id id) {
    return null;
  }

  @Override
  public boolean updateInvoice() {
    return false;
  }

  @Override
  public List<Invoice> getInvoices() {
    return null;
  }
}
