package pl.coderstrust.taxservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderstrust.model.PaymentType;
import pl.coderstrust.service.CompanyService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class TaxSummary {

  private TaxCalculatorService taxCalculatorService;
  private CompanyService companyService;

  @Autowired
  public TaxSummary(TaxCalculatorService taxCalculatorService, CompanyService companyService) {
    this.taxCalculatorService = taxCalculatorService;
    this.companyService = companyService;
  }

  public Map<String, BigDecimal> calculateTaxes(long companyId, int year) {
    LocalDate startDate = LocalDate.of(year, 1, 1);
    LocalDate endDate = LocalDate.of(year, 12, 31);
    final BigDecimal income = taxCalculatorService.calculateIncome(companyId, startDate, endDate);
    final BigDecimal costs = taxCalculatorService.calculateCost(companyId, startDate, endDate);
    Map<String, BigDecimal> taxesSummary = new LinkedHashMap<>();
    taxesSummary.put("Income", income);
    taxesSummary.put("Costs", costs);
    taxesSummary.put("Income - Costs", income.subtract(costs));
    taxesSummary.put("Pension Insurance monthly rate", Rates.getPensionInsurance());
    taxesSummary.put("Pension insurance paid ",
        taxCalculatorService.calculateSpecifiedTypeCostsBetweenDates(companyId,
            startDate, endDate.plusDays(19), PaymentType.PENSION_INSURANCE));
    BigDecimal taxBase = taxCalculatorService.caluclateTaxBase(companyId, startDate, endDate);
    taxesSummary.put("Tax calculation base ", taxBase);
    BigDecimal incomeTax = BigDecimal.valueOf(-1);
    switch (companyService.findEntry(companyId).getTaxType()) {
      case LINEAR: {
        incomeTax = taxBase.multiply(Rates.getLinearTaxRate());
        taxesSummary.put("Income tax", incomeTax);
        break;
      }
      case PROGRESIVE: {
        if (taxBase.compareTo(Rates.getProgressiveTaxRateTreshold()) > 0) {
          incomeTax = Rates.getProgressiveTaxRateTreshold()
              .multiply(Rates.getProgressiveTaxRateTresholdLowPercent())
              .add(taxBase.subtract(Rates.getProgressiveTaxRateTreshold())
                  .multiply(Rates.getProgressiveTaxRateTresholdHighPercent()));
          taxesSummary.put("Income tax", incomeTax);
        } else {
          incomeTax = taxBase.multiply(Rates.getProgressiveTaxRateTresholdLowPercent());
          taxesSummary.put("Income tax", incomeTax);
          taxesSummary.put("Decreasing tax amount ", Rates.getDecreasingTaxAmount());
          incomeTax = incomeTax.subtract(Rates.getDecreasingTaxAmount());
          taxesSummary.put("Income tax - Decreasing tax amount ",
              incomeTax);
        }
        break;
      }
      default: {
        return null;
      }
    }
    BigDecimal healthInsurancePaid = taxCalculatorService.calculateSpecifiedTypeCostsBetweenDates(
        companyId, startDate, endDate.plusDays(19), PaymentType.HEALTH_INSURANCE);
    BigDecimal incomeTaxPaid = taxCalculatorService.calculateSpecifiedTypeCostsBetweenDates(
        companyId, startDate, endDate.plusDays(19), PaymentType.INCOME_TAX_ADVANCE);
    taxesSummary.put("Income tax paid", incomeTaxPaid);
    taxesSummary.put("Health insurance paid", healthInsurancePaid);
    taxesSummary.put("Income tax - health insurance paid - income tax paid",
        incomeTax.subtract(healthInsurancePaid).subtract(incomeTaxPaid));
    return taxesSummary;
  }
}