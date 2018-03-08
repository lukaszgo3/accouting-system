package pl.coderstrust.service;

import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderstrust.database.Database;
import pl.coderstrust.model.Invoice;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class InvoiceBook {

  private static final LocalDate MIN_DATE = LocalDate.of(1500, 11, 12);
  private static final LocalDate MAX_DATE = LocalDate.of(3000, 11, 12);


  @Resource(name="withInvoice")
  private Database <Invoice> dbInvoices;

//  @Resource(name="withCompany")
//  private Database <Company> dbCompanies;

  @Autowired
  InvoiceBook(){
  }

  /**
   * Methods adds invoice.
   *
   * @param invoice invoice to be added.
   */
  public long addInvoice(Invoice invoice) {
    setDefaultInvoiceNameIfEmpty(invoice);
    return dbInvoices.addEntry(invoice);
  }

  /**
   * Removes invoices.
   *
   * @param id invoice id to be removed.
   */
  public void deleteInvoice(long id) {
    dbInvoices.deleteEntry(id);
  }

  /**
   * Method that finds invoices.
   *
   * @param id invoice id to be found
   * @return invoice found
   */
  public Invoice findInvoice(long id) {
    return (Invoice) dbInvoices.getEntryById(id);
  }

  /**
   * Updates existing invoice.
   *
   * @param invoice new invoice that replaces the existing one
   */
  public void updateInvoice(Invoice invoice) {
    setDefaultInvoiceNameIfEmpty(invoice);
    dbInvoices.updateEntry(invoice);
  }

  public List<Invoice> getInvoiceByDate(LocalDate beginDate, LocalDate endDate) {
    if (beginDate == null) {
      beginDate = MIN_DATE;
    }
    if (endDate == null) {
      endDate = MAX_DATE;
    }
    List<Invoice> selectedInvoices = new ArrayList<>();
    List<Invoice> allInvoices = dbInvoices.getEntries();
    for (Invoice invoice : allInvoices) {
      if (invoice.getIssueDate().isBefore(endDate.plusDays(1)) && invoice.getIssueDate()
          .isAfter(beginDate.minusDays(1))) {
        selectedInvoices.add(invoice);
      }
    }
    return selectedInvoices;
  }

  public List<Invoice> getInvoices() {
    return dbInvoices.getEntries();
  }

  public boolean idExist(long id) {
    return dbInvoices.idExist(id);
  }

  private void setDefaultInvoiceNameIfEmpty(Invoice invoice) {
    if (invoice.getInvoiceName() == null || invoice.getInvoiceName().trim().length() == 0) {
      invoice.setInvoiceName(String.format("%d / %s", invoice.getId(), invoice.getIssueDate()));
    }
  }
}
