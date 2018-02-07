package pl.coderstrust.service;

import pl.coderstrust.database.Database;
import pl.coderstrust.database.database.memory.InMemoryDatabase;
import pl.coderstrust.model.Invoice;

public class InvoiceBook {

  private Database database = new InMemoryDatabase();
  private static long currentInvoiceNumber = 0;

  /**
   * Method add Object Invoice to db.
   */
  public void addInvoice() {
    Invoice invoice = new Invoice();
    database.addInvoice(invoice);
    currentInvoiceNumber++;
  }

  //REmove,Find,Update
}
