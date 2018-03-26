package pl.coderstrust.service;

import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.coderstrust.model.Invoice;

import java.time.LocalDate;

@RequestMapping("v2/customer/{customerId}/invoice")
@RestController
public class invoiceControllerByCompany extends AbstractController<Invoice> {
  @RequestMapping(value = "", method = RequestMethod.POST)
  @ApiOperation(value = "Adds the invoice and returning id validating company")
  public ResponseEntity addInvoicePerCompany(
      @PathVariable("customerId") Long customerId,
      @RequestBody Invoice invoice) {
    return super.addEntry(invoice, customerId);
  }

  @RequestMapping(value = "/{invoiceId}", method = RequestMethod.GET)
  @ApiOperation(value = "Returns the invoice by id validating company")
  public ResponseEntity getInvoicePerCompany(
      @PathVariable("customerId") Long invoiceId,
      @PathVariable("invoiceId") Long customerId) {
    return super.getEntryById(invoiceId, customerId);
  }

  @RequestMapping(value = "", method = RequestMethod.GET)
  @ApiOperation(value = "Returns the list of invoices in the specified date range validating company")
  public ResponseEntity getInvoiceByDatePerCompany(
      @PathVariable("customerId") Long customerId,
      @RequestParam(name = "startDate", required = false) LocalDate startDate,
      @RequestParam(name = "endDate", required = false) LocalDate endDate) {
    return super.getEntryByDate(startDate, endDate, customerId);
  }

  @RequestMapping(value = "/{invoiceId}", method = RequestMethod.PUT)
  @ApiOperation(value = "Updates the invoices by id validating company")
  public ResponseEntity updateInvoicePerCustomer(
      @PathVariable("invoiceId") Long invoiceId,
      @PathVariable("customerId") Long customerId,
      @RequestBody Invoice invoice) {
    return super.updateEntry(invoiceId, invoice, customerId);
  }

  @RequestMapping(value = "/{invoiceId}", method = RequestMethod.DELETE)
  @ApiOperation(value = "Deletes the invoices by id, validating company")
  public ResponseEntity removeInvoicePerCustomer(
      @PathVariable("invoiceId") Long invoiceId,
      @PathVariable("customerId") Long customerId) {
    return super.removeEntry(invoiceId, customerId);
  }
}
