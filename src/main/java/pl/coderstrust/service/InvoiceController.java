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

@RestController
public class InvoiceController extends AbstractController<Invoice> {

  public InvoiceController(InvoiceService invoiceService, InvoiceByCompanyFilter byCustomerFilter) {
    super.service = invoiceService;
    super.byCustomerFilter = byCustomerFilter;
  }

  @RequestMapping(value = "v1/invoice", method = RequestMethod.POST)
  @ApiOperation(value = "Adds the invoice and returning id")
  public ResponseEntity addInvoice(
      @RequestBody Invoice invoice) {
    return super.addEntry(invoice, null);
  }

  @RequestMapping(value = "v2/customer/{customerId}/invoice", method = RequestMethod.POST)
  @ApiOperation(value = "Adds the invoice and returning id validating company")
  public ResponseEntity addInvoicePerCompany(
      @PathVariable("customerId") Long customerId,
      @RequestBody Invoice invoice) {
    return super.addEntry(invoice, customerId);
  }

  @RequestMapping(value = "v1/invoice/{id}", method = RequestMethod.GET)
  @ApiOperation(value = "Returns the invoice by id")
  public ResponseEntity getInvoice(
    @PathVariable("id") Long invoiceId) {
    return super.getEntryById(invoiceId, null);
  }

  @RequestMapping(value = "v2/customer{customerId}/invoice/{invoiceId}", method = RequestMethod.GET)
  @ApiOperation(value = "Returns the invoice by id validating company")
  public ResponseEntity getInvoicePerCompany(
      @PathVariable("customerId") Long invoiceId,
      @PathVariable("invoiceId") Long customerId) {
    return super.getEntryById(invoiceId, customerId);
  }

  @RequestMapping(value = "v1/invoice", method = RequestMethod.GET)
  @ApiOperation(value = "Returns the list of invoices in the specified date range")
  public ResponseEntity getInvoiceByDate(
      @RequestParam(name = "startDate", required = false) LocalDate startDate,
      @RequestParam(name = "endDate", required = false) LocalDate endDate) {
    return super.getEntryByDate(startDate, endDate, null);
  }

  @RequestMapping(value = "v2/customer/{customerId}/invoice", method = RequestMethod.GET)
  @ApiOperation(value = "Returns the list of invoices in the specified date range validating company")
  public ResponseEntity getInvoiceByDatePerCompany(
      @PathVariable("customerId") Long customerId,
      @RequestParam(name = "startDate", required = false) LocalDate startDate,
      @RequestParam(name = "endDate", required = false) LocalDate endDate) {
    return super.getEntryByDate(startDate, endDate, customerId);
  }

  @RequestMapping(value = "/v1/invoice/{id}", method = RequestMethod.PUT)
  @ApiOperation(value = "Updates the invoices by id")
  public ResponseEntity updateInvoice(
      @PathVariable("id") Long id,
      @RequestBody Invoice invoice) {
    return super.updateEntry(id, invoice, null);
  }

  @RequestMapping(value = "/v2/customer/{customerId}/invoice/{invoiceId}", method = RequestMethod.PUT)
  @ApiOperation(value = "Updates the invoices by id validating company")
  public ResponseEntity updateInvoicePerCustomer(
      @PathVariable("invoiceId") Long invoiceId,
      @PathVariable("customerId") Long customerId,
      @RequestBody Invoice invoice) {
    return super.updateEntry(invoiceId, invoice, customerId);
  }

  @RequestMapping(value = {"/v1/invoice/{id}"}, method = RequestMethod.DELETE)
  @ApiOperation(value = "Deletes the invoices by id")
  public ResponseEntity removeInvoice(
      @PathVariable("id") Long id) {
    return removeEntry(id, null);
  }

  @RequestMapping(value = {"/v2/customer/{customerId}/invoice/{invoiceId}"}, method = RequestMethod.DELETE)
  @ApiOperation(value = "Deletes the invoices by id, validating company")
  public ResponseEntity removeInvoicePerCustomer(
      @PathVariable("invoiceId") Long invoiceId,
      @PathVariable("customerId") Long customerId) {
    return super.removeEntry(invoiceId, customerId);
  }
}
