package pl.coderstrust.service;

import pl.coderstrust.database.Database;
import pl.coderstrust.database.database.memory.InMemoryDatabase;
import pl.coderstrust.model.Company;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.InvoiceEntry;
import pl.coderstrust.model.PaymentState;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

public class InvoiceBook {

  private Database database = new InMemoryDatabase();
  private static long currentInvoiceNumber = 0;

  /**
   * Method add Object Invoice to db.
   */
  public void addInvoice(String idVisible, Company buyer, Company seller,
      int issueDateDay, int issueDateMonth, int issueDateYear,
      List<InvoiceEntry> products, PaymentState paymentState) {

    // validateVisibleId(); cant already exist in db

    LocalDate issueDate = LocalDate.of(issueDateYear, issueDateMonth, issueDateDay);

    Invoice invoice = new Invoice(idVisible, buyer, seller,
        issueDate, issueDate.plusDays(15), products, paymentState);

    invoice.setSystemId(generateSystemId());
    database.addInvoice(invoice);
    currentInvoiceNumber++;
  }

  /**
   * Methods adds invoice.
   *
   * @param invoice invoice to be added.
   */
  public void addInvoice(Invoice invoice) {
    if (visibleIdExist(invoice.getVisibleId())){
      throw new NoSuchElementException("Visible Id already exist");
    }
    invoice.setSystemId(generateSystemId());
    invoice.setPaymentDate(invoice.getIssueDate().plusDays(14));

    database.addInvoice(invoice);
    currentInvoiceNumber++;
  }

  /**
   * Removes invoices.
   *
   * @param idVisible invoice id to be removed.
   */
  public void removeInvoice(String idVisible) {
    database.deleteInvoiceById(getSystemIdByVisibleId(idVisible));
  }

  /**
   * Method that finds invoices.
   *
   * @param idVisible invoice id to be found
   * @return invoice found
   */
  public Invoice findInvoice(String idVisible) {
    return database.getInvoiceById(getSystemIdByVisibleId(idVisible));
  }

  /**
   * Updates existing invoice.
   *
   * @param invoice new invoice that replaces the existing one
   */
  public void updateInovoice(Invoice invoice) {
    removeInvoice(invoice.getVisibleId());
    database.addInvoice(invoice);
  }


  private long generateSystemId() {
    return currentInvoiceNumber + 1;
  }

  //todo
  private boolean visibleIdExist(String visibleId) {
    List<Invoice> list = database.getInvoices();
    for (Invoice invoice : list) {
      if (invoice.getVisibleId().equals(visibleId)) {
        return true;
      }
    }
    return false;
  }

  private long getSystemIdByVisibleId(String idVisible) {
    List<Invoice> list = database.getInvoices();
    for (int i = 0; i < list.size(); i++) {
      if (list.get(i).getVisibleId().equals(idVisible)) {
        return list.get(i).getSystemId();
      }
    }
    throw new NoSuchElementException("There is no invoice with id : " + idVisible);
  }
}
