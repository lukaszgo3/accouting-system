package pl.coderstrust.service;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.coderstrust.model.Invoice;

@RestController
@RequestMapping("invoice")
public class InvoiceController extends abstractController<Invoice> {

  public InvoiceController(InvoiceService invoiceService) {
    super.service = invoiceService;
  }
}
