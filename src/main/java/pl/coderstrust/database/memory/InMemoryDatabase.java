package pl.coderstrust.database.memory;

import pl.coderstrust.database.Database;
import pl.coderstrust.database.DbException;
import pl.coderstrust.database.ExceptionMsg;
import pl.coderstrust.model.Invoice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryDatabase implements Database {

  private static final int INITIAl_ID = 0;
  private static final int ID_INCREMENT = 1;


  private HashMap<Long, Invoice> invoices = new HashMap<>();
  long lastId = INITIAl_ID;

  @Override
  public long addInvoice(Invoice invoice) {
    invoice.setId(getNextId());
    invoices.put(invoice.getId(), invoice);
    return invoice.getId();
  }

  private long getNextId() {
    return (lastId += ID_INCREMENT);
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