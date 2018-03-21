package pl.coderstrust.taxservice;

import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

  private final Logger logger = LoggerFactory.getLogger(TaxCalculatorController.class);
  private TaxCalculatorService taxService;

  @Autowired
  public TaxCalculatorController(TaxCalculatorService taxService) {
    this.taxService = taxService;
  }

  @RequestMapping(value = "income", method = RequestMethod.GET)
  @ApiOperation(value = "Returns income in specific date range")
  public ResponseEntity calculateIncome(
      @RequestParam(value = "companyName") String companyName,
      @RequestParam(value = "startDate") LocalDate startDate,
      @RequestParam(value = "endDate") LocalDate endDate
  ) {
    if (endDate.isAfter(startDate) || endDate.isEqual(startDate)) {
      logger.info("Response from calculateIncome: "
          + ResponseEntity.ok(taxService.calculateIncome(companyName, startDate, endDate)
          .setScale(2, RoundingMode.HALF_UP)));
      return ResponseEntity.ok(taxService.calculateIncome(companyName, startDate, endDate)
          .setScale(2, RoundingMode.HALF_UP));
    }
    logger.info("Response from calculateIncome: "
        + ResponseEntity.badRequest().body(Messages.END_BEFORE_START));
    return ResponseEntity.badRequest().body(Messages.END_BEFORE_START);
  }

  @RequestMapping(value = "cost", method = RequestMethod.GET)
  @ApiOperation(value = "Returns cost in specific date range")
  public ResponseEntity calculateCost(
      @RequestParam(value = "companyName") String companyName,
      @RequestParam(value = "startDate") LocalDate startDate,
      @RequestParam(value = "endDate") LocalDate endDate
  ) {
    if (endDate.isAfter(startDate) || endDate.isEqual(startDate)) {
      logger.info("Response from calculateCost: "
          + ResponseEntity.ok(taxService.calculateCost(companyName, startDate, endDate)
          .setScale(2, RoundingMode.HALF_UP)));
      return ResponseEntity.ok(taxService.calculateCost(companyName, startDate, endDate)
          .setScale(2, RoundingMode.HALF_UP));
    }
    logger.info("Response from calculateCost: "
        + ResponseEntity.badRequest().body(Messages.END_BEFORE_START));
    return ResponseEntity.badRequest().body(Messages.END_BEFORE_START);
  }

  @RequestMapping(value = "incomeTax", method = RequestMethod.GET)
  @ApiOperation(value = "Returns income Tax in specific date range")
  public ResponseEntity calculateIncomeTax(
      @RequestParam(value = "companyName") String companyName,
      @RequestParam(value = "startDate") LocalDate startDate,
      @RequestParam(value = "endDate") LocalDate endDate
  ) {
    if (endDate.isAfter(startDate) || endDate.isEqual(startDate)) {
      BigDecimal income = taxService.calculateIncome(companyName, startDate, endDate);
      BigDecimal cost = taxService.calculateCost(companyName, startDate, endDate);
      logger.info("Response from calculateIncomeTax: "
          + ResponseEntity.ok(income.subtract(cost).setScale(2, RoundingMode.HALF_UP)));
      return ResponseEntity.ok(income.subtract(cost).setScale(2, RoundingMode.HALF_UP));
    }
    logger.info("Response from calculateIncomeTax: "
        + ResponseEntity.badRequest().body(Messages.END_BEFORE_START));
    return ResponseEntity.badRequest().body(Messages.END_BEFORE_START);
  }

  @RequestMapping(value = "incVat", method = RequestMethod.GET)
  @ApiOperation(value = "Returns income Vat in specific date range")
  public ResponseEntity calculateIncomeVat(
      @RequestParam(value = "companyName") String companyName,
      @RequestParam(value = "startDate") LocalDate startDate,
      @RequestParam(value = "endDate") LocalDate endDate
  ) {

    if (endDate.isAfter(startDate) || endDate.isEqual(startDate)) {
      logger.info("Response from calculateIncomeVat: "
          + ResponseEntity.ok(taxService.calculateIncomeVat(companyName, startDate, endDate)
          .setScale(2, RoundingMode.HALF_UP)));
      return ResponseEntity.ok(taxService.calculateIncomeVat(companyName, startDate, endDate)
          .setScale(2, RoundingMode.HALF_UP));
    }
    logger.info("Response from calculateIncomeVat: "
        + ResponseEntity.badRequest().body(Messages.END_BEFORE_START));
    return ResponseEntity.badRequest().body(Messages.END_BEFORE_START);
  }

  @RequestMapping(value = "outVat", method = RequestMethod.GET)
  @ApiOperation(value = "Returns outcome Vat in specific date range")
  public ResponseEntity calculateOutcomeVat(
      @RequestParam(value = "companyName") String companyName,
      @RequestParam(value = "startDate") LocalDate startDate,
      @RequestParam(value = "endDate") LocalDate endDate
  ) {
    if (endDate.isAfter(startDate) || endDate.isEqual(startDate)) {
      logger.info("Response from calculateOutcomeVat: "
          + ResponseEntity.ok(taxService.calculateOutcomeVat(companyName, startDate, endDate)
          .setScale(2, RoundingMode.HALF_UP)));
      return ResponseEntity.ok(taxService.calculateOutcomeVat(companyName, startDate, endDate)
          .setScale(2, RoundingMode.HALF_UP));
    }
    logger.info("Response from calculateOutcomeVat: "
        + ResponseEntity.badRequest().body(Messages.END_BEFORE_START));
    return ResponseEntity.badRequest().body(Messages.END_BEFORE_START);
  }

  @RequestMapping(value = "diffVat", method = RequestMethod.GET)
  @ApiOperation(value = "Returns difference in Vat in specific date range")
  public ResponseEntity calculateDifferenceVat(
      @RequestParam(value = "companyName") String companyName,
      @RequestParam(value = "startDate") LocalDate startDate,
      @RequestParam(value = "endDate") LocalDate endDate
  ) {
    if (endDate.isAfter(startDate) || endDate.isEqual(startDate)) {
      BigDecimal outVat = taxService.calculateOutcomeVat(companyName, startDate, endDate);
      BigDecimal incVat = taxService.calculateIncomeVat(companyName, startDate, endDate);
      logger.info("Response from calculateDifferenceVat: "
          + ResponseEntity.ok(outVat.subtract(incVat).setScale(2, RoundingMode.HALF_UP)));
      return ResponseEntity.ok(outVat.subtract(incVat).setScale(2, RoundingMode.HALF_UP));
    }
    logger.info("Response from calculateDifferenceVat: "
        + ResponseEntity.badRequest().body(Messages.END_BEFORE_START));
    return ResponseEntity.badRequest().body(Messages.END_BEFORE_START);
  }
}