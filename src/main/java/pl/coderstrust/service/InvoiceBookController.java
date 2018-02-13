package pl.coderstrust.service;

import org.apache.tomcat.jni.Local;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.coderstrust.model.Invoice;

import java.time.LocalDate;
import java.util.List;

@RestController
public class InvoiceBookController {

  private InvoiceBook ib = new InvoiceBook();
  private DataValidator dv = new DataValidator();

  @RequestMapping(value = "addInvoice", method = RequestMethod.POST)
  public List<String> addInvoice(@RequestBody Invoice invoice) {
    if (dv.validateInvoice(invoice).size() == 1) {
      ib.addInvoice(invoice);
    }
    return dv.validateInvoice(invoice);
  }

  @RequestMapping(value = "getInvoice/{visibleId}", method = RequestMethod.GET)
  public Invoice getInvoiceById(@PathVariable("visibleId") String visibleId) {
    return ib.findInvoice(visibleId);
  }

  @RequestMapping(value = "getInvoice", method = RequestMethod.GET)
  public List<Invoice> getInvoices() {
    return ib.getInvoices();
  }

  @RequestMapping(value = "getInvoice/bydate/{beginDate}/{endDate}", method = RequestMethod.GET)
  public List<Invoice> getInvoiceByDate(@PathVariable("beginDate") String beginDate, @PathVariable("endDate") String endDate) {
    return ib.getInvoiceByDate(LocalDate.parse(beginDate), LocalDate.parse(endDate));

  }

  @RequestMapping(value = "updateInvoice", method = RequestMethod.PUT)
  public List<String> updateInvoice(@RequestBody Invoice invoice) {
    if (dv.validateInvoice(invoice).size() == 0) {
      ib.updateInovoice(invoice);
    }
    return dv.validateInvoice(invoice);
  }

  @RequestMapping(value = "removeInvoice/{visibleId}", method = RequestMethod.DELETE)
  public void remoceInvoice(@PathVariable("visibleId") String visibleId) {
    ib.removeInvoice(visibleId);
  }
}
