package pl.coderstrust.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.coderstrust.model.Invoice;

import java.time.LocalDate;
import java.util.List;

@RestController
public class InvoiceBookController {

  private InvoiceBook invoiceBook = new InvoiceBook();
  private InvoiceValidator InvoiceValidator = new InvoiceValidator();

  @RequestMapping(value = "invoice", method = RequestMethod.POST)
  public ResponseEntity addInvoice(@RequestBody Invoice invoice) {
    List<String> invoiceState = InvoiceValidator.validateInvoice(invoice);
    if (invoiceState.isEmpty()) {
      long id = invoiceBook.addInvoice(invoice);
      return ResponseEntity.accepted().body("Invoice added under id : " + id);
    }
    return ResponseEntity.badRequest().body(invoiceState);
  }

  @RequestMapping(value = "invoice/{id}", method = RequestMethod.GET)
  public ResponseEntity getInvoiceById(@PathVariable("id") long id) {
    if (id < 0) {
      return ResponseEntity.badRequest().body("Invoice id cant be negative.");
    }
    return ResponseEntity.ok(invoiceBook.findInvoice(id));
  }

  @RequestMapping(value = "invoice", method = RequestMethod.GET)
  public ResponseEntity getInvoices() {
    return ResponseEntity.ok(invoiceBook.getInvoices());
  }

  @RequestMapping(value = "invoice/bydate/{beginDate}/{endDate}", method = RequestMethod.GET)
  public ResponseEntity getInvoiceByDate(@PathVariable("beginDate") String beginDate, @PathVariable("endDate") String endDate) {
    return ResponseEntity.ok(invoiceBook.getInvoiceByDate(LocalDate.parse(beginDate), LocalDate.parse(endDate)));
  }

  @RequestMapping(value = "invoice", method = RequestMethod.PUT)
  public ResponseEntity updateInvoice(@RequestBody Invoice invoice) {
    List<String> invoiceState = InvoiceValidator.validateInvoice(invoice);
    if (!invoiceState.isEmpty()) {
      return ResponseEntity.badRequest().body(invoiceState);
    }
    invoiceBook.updateInvoice(invoice);
    return ResponseEntity.ok().body("Invoice updated");
  }

  @RequestMapping(value = "invoice/{id}", method = RequestMethod.DELETE)
  public ResponseEntity removeInvoice(@PathVariable("id") long id) {
    if (id < 0) {
      return ResponseEntity.badRequest().body("Invoice id cant be negative");
    }
    invoiceBook.removeInvoice(id);
    return ResponseEntity.ok().body("Invoice removed");
  }
}
