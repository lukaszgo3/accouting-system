package pl.coderstrust;

import java.util.List;

public interface DataBase {

  boolean addInvoice(Invoice invoice);

  boolean deleteInvoiceById(Id id);

  Invoice getInvoiceById(Id id);

  boolean updateInvoice();

  List<Invoice> getInvoices();

}
