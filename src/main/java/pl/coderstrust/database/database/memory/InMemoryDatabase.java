package pl.coderstrust.database.database.memory;

import pl.coderstrust.database.Database;
import pl.coderstrust.model.Invoice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;

public class InMemoryDatabase implements Database {

  private HashMap<Long, Invoice> invoices = new HashMap<>();

  @Override
  public void addInvoice(Invoice invoice) {
    invoices.put(invoice.getId(), invoice);
  }

  @Override
  public void deleteInvoice(long id) {
    invoices.remove(id);
  }

  @Override
  public Invoice getInvoiceById(long id) {
    return invoices.get(id);
  }

  @Override
  public void updateInvoice(Invoice invoice) {
    invoices.replace(invoice.getId(), invoice);
  }

  @Override
  public List<Invoice> getInvoices() {
    return new ArrayList<>(invoices.values());
  }

  @Override
  public boolean idExist(long id) {
    return invoices.containsKey(id);
  }
}
