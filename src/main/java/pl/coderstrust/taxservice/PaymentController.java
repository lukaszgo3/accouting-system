package pl.coderstrust.taxservice;

import io.swagger.annotations.ApiOperation;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.coderstrust.model.Messages;
import pl.coderstrust.model.Payment;
import pl.coderstrust.model.PaymentType;
import pl.coderstrust.service.CompanyService;

@RestController
@RequestMapping("payment")
public class PaymentController {

  private PaymentService paymentService;
  private CompanyService companyService;

  @Autowired
  public PaymentController(PaymentService paymentService, CompanyService companyService) {
    this.paymentService = paymentService;
    this.companyService = companyService;
  }

  @RequestMapping(value = "/{companyId}", method = RequestMethod.POST)
  @ApiOperation(value = "Adds payments to company payments list.")
  public ResponseEntity addPayment(@PathVariable("companyId") long companyId,
      @RequestBody Payment payment) {
    List<String> paymentState = payment.validate();
    if (!paymentState.isEmpty()) {
      return ResponseEntity.badRequest().body(paymentState);

    }
    long id = paymentService.addPayment(companyId, payment);
    String companyName = companyService.findEntry(companyId).getName();
    return ResponseEntity.ok("Payment added under id " + id +
        " in " + companyName + " payments list.");
  }

  @RequestMapping(value = "/{companyId}", method = RequestMethod.GET)
  @ApiOperation(value = "Returns the list of payments of selected type in" +
      " specified date range and type.")
  public ResponseEntity getEntries(
      @PathVariable("companyId") long companyId,
      @RequestParam(value = "startDate", required = false) LocalDate startDate,
      @RequestParam(value = "endDate", required = false) LocalDate endDate,
      @RequestParam(value = "type", required = false) PaymentType type) {
    if (!companyService.idExist(companyId)) {
      return ResponseEntity.badRequest().body(Messages.COMPANY_NOT_EXIST);
    }
    if (startDate == null && endDate == null && type == null) {
      return ResponseEntity.ok(paymentService.getPayments(companyId));
    }
    if (type == null) {
      return ResponseEntity.ok(paymentService.getPaymentsByDate(companyId, startDate, endDate));
    }
    return ResponseEntity.ok(paymentService.getPaymentsByTypeAndDate(companyId,
        startDate, endDate, type));
  }

  @RequestMapping(value = "/{companyId}/{paymentId}", method = RequestMethod.PUT)
  @ApiOperation(value = "Updates the payment by id.")
  public ResponseEntity updateInvoice(
      @PathVariable("companyId") long companyId,
      @PathVariable("paymentId") long paymentId,
      @RequestBody Payment payment) {
    if (!companyService.idExist(companyId)) {
      return ResponseEntity.badRequest().body(Messages.COMPANY_NOT_EXIST);
    }
    if (!paymentService.idExist(companyId, paymentId)) {
      String companyName = companyService.findEntry(companyId).getName();
      return ResponseEntity.badRequest().body("Payment under " + paymentId + "doesn't exist +"
          + "in company " + companyName + " payments list.");
    }
    List<String> entryState = payment.validate();
    if (!entryState.isEmpty()) {
      return ResponseEntity.badRequest().body(entryState);
    }
    paymentService.updatePayment(companyId, payment);
    return ResponseEntity.ok().build();
  }

  @RequestMapping(value = "/{companyId}/{paymentId}", method = RequestMethod.DELETE)
  @ApiOperation(value = "Remove payment by id.")
  public ResponseEntity removeInvoice(
      @PathVariable("companyId") long companyId,
      @PathVariable("companyId") long paymentId) {
    if (!companyService.idExist(companyId)) {
      return ResponseEntity.badRequest().body(Messages.COMPANY_NOT_EXIST);
    }
    if (!paymentService.idExist(companyId, paymentId)) {
      String companyName = companyService.findEntry(companyId).getName();
      return ResponseEntity.badRequest().body("Payment under " + paymentId + "doesn't exist +"
          + "in company " + companyName + " payments list.");
    }
    paymentService.deletePayment(companyId, paymentId);
    return ResponseEntity.ok().build();
  }
}
