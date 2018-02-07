package pl.coderstrust.service;

import static junit.framework.TestCase.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class InvoiceBookTest {

  InvoiceBook testBook;
  TestCasesGenerator generator;

  @Before
  private void initializeInvoiceBook(){
    testBook = new InvoiceBook();
    generator = new TestCasesGenerator();
  }

  @Test
  public void shouldAddLargeNumberOfInvoices() {

    int invoiceEntriesCount = 100;
    for (int i = 0; i <1e3 ; i++) {
      testBook.addInvoice(generator.getTestInvoice(i, invoiceEntriesCount));
    }
    assertTrue(true);

  }



}

