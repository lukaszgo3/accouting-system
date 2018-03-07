package pl.coderstrust.taxservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.coderstrust.model.Messages;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@RestController
@Configuration
public class TaxCalculatorController {

  private TaxCalculatorService taxService;

  @Autowired
  public TaxCalculatorController(TaxCalculatorService taxService) {
    this.taxService = taxService;
  }

  @RequestMapping(value = "income", method = RequestMethod.GET)
  public ResponseEntity calculateIncome(
      @RequestParam(value = "companyName") String companyName,
      @RequestParam(value = "startDate") LocalDate startDate,
      @RequestParam(value = "endDate") LocalDate endDate
  ) {
    if (endDate.isAfter(startDate) || endDate.isEqual(startDate)) {
      return ResponseEntity.ok(taxService.calculateIncome(companyName, startDate, endDate)
          .setScale(2, RoundingMode.HALF_UP));
    }
    return ResponseEntity.badRequest().body(Messages.END_BEFORE_START);
  }

  @RequestMapping(value = "cost", method = RequestMethod.GET)
  public ResponseEntity calculateCost(
      @RequestParam(value = "companyName") String companyName,
      @RequestParam(value = "startDate") LocalDate startDate,
      @RequestParam(value = "endDate") LocalDate endDate
  ) {
    if (endDate.isAfter(startDate) || endDate.isEqual(startDate)) {
      return ResponseEntity.ok(taxService.calculateCost(companyName, startDate, endDate)
          .setScale(2, RoundingMode.HALF_UP));
    }
    return ResponseEntity.badRequest().body(Messages.END_BEFORE_START);
  }

  @RequestMapping(value = "incomeTax", method = RequestMethod.GET)
  public ResponseEntity calculateIncomeTax(
      @RequestParam(value = "companyName") String companyName,
      @RequestParam(value = "startDate") LocalDate startDate,
      @RequestParam(value = "endDate") LocalDate endDate
  ) {
    if (endDate.isAfter(startDate) || endDate.isEqual(startDate)) {
      BigDecimal income = taxService.calculateIncome(companyName, startDate, endDate);
      BigDecimal cost = taxService.calculateCost(companyName, startDate, endDate);
      return ResponseEntity.ok(income.subtract(cost).setScale(2, RoundingMode.HALF_UP));
    }

    return ResponseEntity.badRequest().body(Messages.END_BEFORE_START);
  }

  @RequestMapping(value = "incVat", method = RequestMethod.GET)
  public ResponseEntity calculateIncomeVat(
      @RequestParam(value = "companyName") String companyName,
      @RequestParam(value = "startDate") LocalDate startDate,
      @RequestParam(value = "endDate") LocalDate endDate
  ) {

    if (endDate.isAfter(startDate) || endDate.isEqual(startDate)) {
      return ResponseEntity.ok(taxService.calculateIncomeVat(companyName, startDate, endDate)
          .setScale(2, RoundingMode.HALF_UP));
    }
    return ResponseEntity.badRequest().body(Messages.END_BEFORE_START);
  }

  @RequestMapping(value = "outVat", method = RequestMethod.GET)
  public ResponseEntity calculateOutcomeVat(
      @RequestParam(value = "companyName") String companyName,
      @RequestParam(value = "startDate") LocalDate startDate,
      @RequestParam(value = "endDate") LocalDate endDate
  ) {
    if (endDate.isAfter(startDate) || endDate.isEqual(startDate)) {
      return ResponseEntity.ok(taxService.calculateOutcomeVat(companyName, startDate, endDate)
          .setScale(2, RoundingMode.HALF_UP));
    }
    return ResponseEntity.badRequest().body(Messages.END_BEFORE_START);
  }

  @RequestMapping(value = "diffVat", method = RequestMethod.GET)
  public ResponseEntity calculateDifferenceVat(
      @RequestParam(value = "companyName") String companyName,
      @RequestParam(value = "startDate") LocalDate startDate,
      @RequestParam(value = "endDate") LocalDate endDate
  ) {
    if (endDate.isAfter(startDate) || endDate.isEqual(startDate)) {
      BigDecimal outVat = taxService.calculateOutcomeVat(companyName, startDate, endDate);
      BigDecimal incVat = taxService.calculateIncomeVat(companyName, startDate, endDate);
      return ResponseEntity.ok(outVat.subtract(incVat).setScale(2, RoundingMode.HALF_UP));
    }
    return ResponseEntity.badRequest().body(Messages.END_BEFORE_START);
  }
}