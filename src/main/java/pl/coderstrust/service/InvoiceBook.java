package pl.coderstrust.service;

import pl.coderstrust.database.Database;
import pl.coderstrust.model.Invoice;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InvoiceBook {

  private Database database;
  private static long currentInvoiceNumber = 0;

  public InvoiceBook() {
  }

  public InvoiceBook(Database database) {
    this.database = database;
  }

  /**
   * Methods adds invoice.
   *
   * @param invoice invoice to be added.
   */
  public long addInvoice(Invoice invoice) {
    invoice.setId(generateSystemId());
    if (invoice.getInvoiceName() == null || invoice.getInvoiceName().trim().length() == 0) {
      invoice.setInvoiceName(String.format("%d / %s", invoice.getId(), invoice.getIssueDate()));
    }
    database.addInvoice(invoice);
    currentInvoiceNumber++;
    return invoice.getId();
  }

  /**
   * Removes invoices.
   *
   * @param id invoice id to be removed.
   */
  public void deleteInvoice(long id) {
    database.deleteInvoice(id);
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
    if (beginDate == null) {
      beginDate = LocalDate.MIN;
    }
    if (endDate == null) {
      endDate = LocalDate.MAX;
    }
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

  public boolean idExist(long id) {
    return database.idExist(id);
  }

  private long generateSystemId() {
    return currentInvoiceNumber + 1;
  }
}
