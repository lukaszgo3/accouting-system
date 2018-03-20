package pl.coderstrust.taxservice;

import io.swagger.annotations.ApiOperation;
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
import pl.coderstrust.service.CompanyService;

@RestController
@Configuration
public class TaxCalculatorController {

  private TaxCalculatorService taxService;
  private TaxSummary taxSummary;
  private CompanyService companyService;

  @Autowired
  public TaxCalculatorController(TaxCalculatorService taxService, TaxSummary taxSummary,
      CompanyService companyService) {
    this.taxService = taxService;
    this.taxSummary = taxSummary;
    this.companyService = companyService;
  }

  //TODO Maybe change companyID from RequestParam to Path Variable will look cleaner?

  @RequestMapping(value = "income", method = RequestMethod.GET)
  @ApiOperation(value = "Returns income in specific date range")
  public ResponseEntity calculateIncome(
      @RequestParam(value = "companyId") long companyId,
      @RequestParam(value = "startDate") LocalDate startDate,
      @RequestParam(value = "endDate") LocalDate endDate
  ) {
    if (endDate.isAfter(startDate) || endDate.isEqual(startDate)) {
      return ResponseEntity.ok(taxService.calculateIncome(companyId, startDate, endDate)
          .setScale(2, RoundingMode.HALF_UP));
    }
    return ResponseEntity.badRequest().body(Messages.END_BEFORE_START);
  }

  @RequestMapping(value = "cost", method = RequestMethod.GET)
  @ApiOperation(value = "Returns cost in specific date range")
  public ResponseEntity calculateCost(
      @RequestParam(value = "companyId") long companyId,
      @RequestParam(value = "startDate") LocalDate startDate,
      @RequestParam(value = "endDate") LocalDate endDate
  ) {
    if (endDate.isAfter(startDate) || endDate.isEqual(startDate)) {
      return ResponseEntity.ok(taxService.calculateCost(companyId, startDate, endDate)
          .setScale(2, RoundingMode.HALF_UP));
    }
    return ResponseEntity.badRequest().body(Messages.END_BEFORE_START);
  }

  @RequestMapping(value = "incomeTax", method = RequestMethod.GET)
  @ApiOperation(value = "Returns income Tax in specific date range")
  public ResponseEntity calculateIncomeTax(
      @RequestParam(value = "companyId") long companyId,
      @RequestParam(value = "startDate") LocalDate startDate,
      @RequestParam(value = "endDate") LocalDate endDate
  ) {
    if (endDate.isAfter(startDate) || endDate.isEqual(startDate)) {
      BigDecimal income = taxService.calculateIncome(companyId, startDate, endDate);
      BigDecimal cost = taxService.calculateCost(companyId, startDate, endDate);
      return ResponseEntity.ok(income.subtract(cost).setScale(2, RoundingMode.HALF_UP));
    }

    return ResponseEntity.badRequest().body(Messages.END_BEFORE_START);
  }

  @RequestMapping(value = "incVat", method = RequestMethod.GET)
  @ApiOperation(value = "Returns income Vat in specific date range")
  public ResponseEntity calculateIncomeVat(
      @RequestParam(value = "companyId") long companyId,
      @RequestParam(value = "startDate") LocalDate startDate,
      @RequestParam(value = "endDate") LocalDate endDate
  ) {
    if (endDate.isAfter(startDate) || endDate.isEqual(startDate)) {
      return ResponseEntity.ok(taxService.calculateIncomeVat(companyId, startDate, endDate)
          .setScale(2, RoundingMode.HALF_UP));
    }
    return ResponseEntity.badRequest().body(Messages.END_BEFORE_START);
  }

  @RequestMapping(value = "outVat", method = RequestMethod.GET)
  @ApiOperation(value = "Returns outcome Vat in specific date range")
  public ResponseEntity calculateOutcomeVat(
      @RequestParam(value = "companyId") long companyId,
      @RequestParam(value = "startDate") LocalDate startDate,
      @RequestParam(value = "endDate") LocalDate endDate
  ) {
    if (endDate.isAfter(startDate) || endDate.isEqual(startDate)) {
      return ResponseEntity.ok(taxService.calculateOutcomeVat(companyId, startDate, endDate)
          .setScale(2, RoundingMode.HALF_UP));
    }
    return ResponseEntity.badRequest().body(Messages.END_BEFORE_START);
  }

  @RequestMapping(value = "diffVat", method = RequestMethod.GET)
  @ApiOperation(value = "Returns difference in Vat in specific date range")
  public ResponseEntity calculateDifferenceVat(
      @RequestParam(value = "companyId") long companyId,
      @RequestParam(value = "startDate") LocalDate startDate,
      @RequestParam(value = "endDate") LocalDate endDate
  ) {
    if (endDate.isAfter(startDate) || endDate.isEqual(startDate)) {
      BigDecimal outVat = taxService.calculateOutcomeVat(companyId, startDate, endDate);
      BigDecimal incVat = taxService.calculateIncomeVat(companyId, startDate, endDate);
      return ResponseEntity.ok(outVat.subtract(incVat).setScale(2, RoundingMode.HALF_UP));
    }
    return ResponseEntity.badRequest().body(Messages.END_BEFORE_START);
  }

  @RequestMapping(value = "taxSummary", method = RequestMethod.GET)
  @ApiOperation(value = "Returns taxes summary in specific date range")
  public ResponseEntity calculateTaxSummary(
      @RequestParam(value = "companyId") long companyId,
      @RequestParam(value = "year") int year) {
    if (year > LocalDate.now().getYear() + 50 || year < LocalDate.now().getYear() - 200) {
      return ResponseEntity.badRequest().body(Messages.WRONG_YEAR);
    }

    return ResponseEntity.ok(taxSummary.calculateTaxes(companyId, year));
  }

  //TODO is this validation enough?
  @RequestMapping(value = "incomeTaxAdvance", method = RequestMethod.GET)
  @ApiOperation(value = "Returns value of income tax advance in specific date range")
  public ResponseEntity calculateIncomeTaxAdvance(
      @RequestParam(value = "companyId") long companyId,
      @RequestParam(value = "startDate") LocalDate startDate,
      @RequestParam(value = "endDate") LocalDate endDate
  ) {
    if ((startDate.isAfter(endDate)) || (endDate.getYear() != startDate.getYear())) {
      return ResponseEntity.badRequest().body(Messages.END_BEFORE_START);
    }
    return ResponseEntity.ok(
        taxService.calculateIncomeTaxAdvance(companyId, startDate, endDate)
            .setScale(2, RoundingMode.HALF_UP));
  }

}