package pl.coderstrust.database.database.memory;

import pl.coderstrust.database.Database;
import pl.coderstrust.database.DbException;
import pl.coderstrust.database.ExceptionMsg;
import pl.coderstrust.model.Invoice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryDatabase implements Database {

  private HashMap<Long, Invoice> invoices = new HashMap<>();

  @Override
  public void addInvoice(Invoice invoice) {
    invoices.put(invoice.getId(), invoice);
  }

  @Override
  public void deleteInvoice(long id) {
    if (!idExist(id)) {
      throw new DbException(ExceptionMsg.INVOICE_NOT_EXIST);
    }

    invoices.remove(id);
  }

  @Override
  public Invoice getInvoiceById(long id) {
    if (!idExist(id)) {
      throw new DbException(ExceptionMsg.INVOICE_NOT_EXIST);
    }
    return invoices.get(id);
  }

  @Override
  public void updateInvoice(Invoice invoice) {
    if (!idExist(invoice.getId())) {
      throw new DbException(ExceptionMsg.INVOICE_NOT_EXIST);
    }

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