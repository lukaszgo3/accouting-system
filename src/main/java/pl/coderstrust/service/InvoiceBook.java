package pl.coderstrust.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pl.coderstrust.database.Database;
import pl.coderstrust.model.Invoice;

@Service
public class InvoiceBook extends Book<Invoice> {


  @Autowired
  public InvoiceBook(@Qualifier("withInvoices") Database<Invoice> dbInvoices) {
    super.database = dbInvoices;
  }
}
