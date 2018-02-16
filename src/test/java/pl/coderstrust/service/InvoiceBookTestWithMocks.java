package pl.coderstrust.service;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pl.coderstrust.database.Database;
import pl.coderstrust.model.Invoice;

import java.time.LocalDate;
import java.util.Collections;

@RunWith(MockitoJUnitRunner.class)
public class InvoiceBookTestWithMocks {

  @Mock
  private Database database;

  @Mock
  private Invoice invoice;

  @Test
  public void shouldAddInvoice() {
    InvoiceBook invoiceBook = new InvoiceBook(database);
    invoiceBook.addInvoice(invoice);
    verify(database, times(1)).addInvoice(invoice);
  }

  @Test
  public void shouldRemoveInvoice() {
    InvoiceBook invoiceBook = new InvoiceBook(database);
    invoiceBook.deleteInvoice(1);
    verify(database, times(1)).deleteInvoice(anyLong());
  }

  @Test
  public void shouldFindInvoice() {
    InvoiceBook invoiceBook = new InvoiceBook(database);
    invoiceBook.findInvoice(1);
    verify(database, times(1)).getInvoiceById(anyLong());
  }

  @Test
  public void shouldUpdateInvoice() {
    InvoiceBook invoiceBook = new InvoiceBook(database);
    invoiceBook.updateInvoice(invoice);
    verify(database, times(1)).updateInvoice(invoice);
  }

  @Test
  public void shouldGetInvoice() {
    InvoiceBook invoiceBook = new InvoiceBook(database);
    when(database.getInvoices()).thenReturn(Collections.singletonList(invoice));
    invoiceBook.getInvoices();
    verify(database).getInvoices();
  }

  @Test
  public void shouldCheckIdExist() {
    InvoiceBook invoiceBook = new InvoiceBook(database);
    when(database.idExist(anyLong())).thenReturn(true);
    invoiceBook.idExist(1);
    verify(database).idExist(anyLong());
  }

  @Test
  public void shouldGetInvoiceByDate() {
    LocalDate date = LocalDate.of(2018, 3, 15);
    InvoiceBook invoiceBook = new InvoiceBook(database);
    Invoice invoice1 = new Invoice();
    invoice1.setIssueDate(date);

    when(database.getInvoices()).thenReturn(Collections.singletonList(invoice1));
    invoiceBook.getInvoiceByDate(date, date);
    verify(database, times(1)).getInvoices();
  }
}