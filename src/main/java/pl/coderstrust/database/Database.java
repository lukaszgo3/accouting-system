package pl.coderstrust.database;

import java.util.List;
import pl.coderstrust.model.Invoice;

public interface Database {

  long addInvoice(Invoice invoice);

  void deleteInvoiceById(long id);

  Invoice getInvoiceById(long id);

  void updateInvoice(Invoice invoice);

  List<Invoice> getInvoices();

}
