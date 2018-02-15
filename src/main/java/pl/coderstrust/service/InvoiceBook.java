package pl.coderstrust.service;

import pl.coderstrust.database.Database;
import pl.coderstrust.database.database.memory.InMemoryDatabase;
import pl.coderstrust.model.Invoice;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InvoiceBook {

  private Database database = new InMemoryDatabase();
  private static long currentInvoiceNumber = 0;

  /**
   * Methods adds invoice.
   *
   * @param invoice invoice to be added.
   */
  public long addInvoice(Invoice invoice) {
    invoice.setId(generateSystemId());
    invoice.setPaymentDate(invoice.getIssueDate().plusDays(14));
    database.addInvoice(invoice);
    currentInvoiceNumber++;
    return invoice.getId();
  }

  /**
   * Removes invoices.
   *
   * @param id invoice id to be removed.
   */
  public void removeInvoice(long id) {
    database.deleteInvoiceById(id);
  }

  /**
   * Method that finds invoices.
   *
   * @param id invoice id to be found
   * @return invoice found
   */
  public Invoice findInvoice(long id) {
    return database.getInvoiceById(id);
  }

  /**
   * Updates existing invoice.
   *
   * @param invoice new invoice that replaces the existing one
   */
  public void updateInvoice(Invoice invoice) {
    database.updateInvoice(invoice);
  }

  public List<Invoice> getInvoiceByDate(LocalDate beginDate, LocalDate endDate) {
    List<Invoice> selectedInvoices = new ArrayList<>();
    List<Invoice> allInvoices = database.getInvoices();
    for (Invoice invoice : allInvoices) {
      if (invoice.getIssueDate().isBefore(endDate) && invoice.getIssueDate().isAfter(beginDate)) {
        selectedInvoices.add(invoice);
      }
    }
    return selectedInvoices;
  }

  public List<Invoice> getInvoices() {
    return database.getInvoices();
  }

  private long generateSystemId() {
    return currentInvoiceNumber + 1;
  }
}
