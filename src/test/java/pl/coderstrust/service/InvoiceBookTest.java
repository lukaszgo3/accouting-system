package pl.coderstrust.service;

import org.junit.Before;
import org.junit.Test;
import pl.coderstrust.model.Invoice;

import static org.junit.Assert.assertArrayEquals;

public class InvoiceBookTest {

  private InvoiceBook testBook;
  private TestCasesGenerator generator;

  @Before
  public void initializeInvoiceBook() {
    testBook = new InvoiceBook();
    generator = new TestCasesGenerator();
  }

  @Test
  public void shouldAddLargeNumberOfInvoices() {

    int invoiceEntriesCount = 10;
    int invoicesCount = 1_0;
    Invoice[] invoices = new Invoice[invoicesCount];
    String[] invoiceIds = new String[invoicesCount];
    for (int i = 0; i < invoicesCount; i++) {
      invoices[i] = generator.getTestInvoice(i, invoiceEntriesCount);
      testBook.addInvoice(invoices[i]);
      invoiceIds[i] = invoices[i].getVisibleId();
    }

    Invoice[] output = new Invoice[invoicesCount];

    for (int i = 0; i < invoicesCount; i++) {
      output[i] = testBook.findInvoice(invoiceIds[i]);
    }
    assertArrayEquals(output, invoices);
  }


}

