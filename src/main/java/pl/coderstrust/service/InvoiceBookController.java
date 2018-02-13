package pl.coderstrust.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.coderstrust.model.Invoice;

import java.time.LocalDate;
import java.util.List;

@RestController
public class InvoiceBookController {

  private InvoiceBook invoiceBook = new InvoiceBook();
  private invoiceValidator invoiceValidator = new invoiceValidator();

  @RequestMapping(value = "invoice", method = RequestMethod.POST)
  public ResponseEntity addInvoice(@RequestBody Invoice invoice) {
    List<String> invoiceState = invoiceValidator.validateInvoice(invoice);
    if (invoiceState.size() == 1) {
      invoiceBook.addInvoice(invoice);
      return new ResponseEntity<>("Invoice Added", HttpStatus.ACCEPTED);
    }
    return new ResponseEntity<>(invoiceState, HttpStatus.BAD_REQUEST);
  }

  @RequestMapping(value = "invoice/{visibleId}", method = RequestMethod.GET)
  public ResponseEntity getInvoiceById(@PathVariable("visibleId") String visibleId) {
    if (visibleId == null || visibleId.trim().length() == 0) {
      return new ResponseEntity<>("ID is empty", HttpStatus.BAD_REQUEST);
    }
    Invoice invoice = invoiceBook.findInvoice(visibleId);
    return new ResponseEntity<>(invoice, HttpStatus.OK);
  }

  @RequestMapping(value = "invoice", method = RequestMethod.GET)
  public ResponseEntity getInvoices() {
    List<Invoice> invoices = invoiceBook.getInvoices();
    if (invoices.size() == 0) {
      return new ResponseEntity<>("There are no invoices in database", HttpStatus.NO_CONTENT);
    }
    return new ResponseEntity<>(invoices, HttpStatus.OK);
  }

  @RequestMapping(value = "invoice/bydate/{beginDate}/{endDate}", method = RequestMethod.GET)
  public ResponseEntity getInvoiceByDate(@PathVariable("beginDate") String beginDate, @PathVariable("endDate") String endDate) {
    List<Invoice> selectedInvoices = invoiceBook.getInvoiceByDate(LocalDate.parse(beginDate), LocalDate.parse(endDate));
    if (selectedInvoices.size() == 0) {
      return new ResponseEntity<>("There are no invoices beetwean selected dates.", HttpStatus.NO_CONTENT);
    }
    return new ResponseEntity<>(selectedInvoices, HttpStatus.OK);
  }

  @RequestMapping(value = "invoice", method = RequestMethod.PUT)
  public ResponseEntity updateInvoice(@RequestBody Invoice invoice) {
    List<String> invoiceState = invoiceValidator.validateInvoice(invoice);
    if (invoiceState.size() != 1) {
      return new ResponseEntity<>(invoiceState, HttpStatus.NOT_ACCEPTABLE);
    }
    invoiceBook.updateInovoice(invoice);
    return new ResponseEntity<>("Invoice updated", HttpStatus.OK);
  }

  @RequestMapping(value = "invoice/{visibleId}", method = RequestMethod.DELETE)
  public ResponseEntity removeInvoice(@PathVariable("visibleId") String visibleId) {
    if (visibleId == null || visibleId.trim().length() == 0) {
      return new ResponseEntity<>("ID is empty", HttpStatus.BAD_REQUEST);
    }
    if (!invoiceBook.visibleIdExist(visibleId)) {
      return new ResponseEntity<>("There is no invoice with this ID", HttpStatus.BAD_REQUEST);
    }
    invoiceBook.removeInvoice(visibleId);
    return new ResponseEntity<>("Invoice deleted", HttpStatus.OK);
  }
}
