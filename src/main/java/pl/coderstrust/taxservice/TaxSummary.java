package pl.coderstrust.taxservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderstrust.model.TaxesType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import pl.coderstrust.service.CompanyService;

@Service
public class TaxSummary {

  private TaxCalculatorService taxCalculatorService;
  private CompanyService companyService;

  @Autowired
  public TaxSummary(TaxCalculatorService taxCalculatorService, CompanyService companyService) {
    this.taxCalculatorService = taxCalculatorService;
    this.companyService = companyService;
  }

  public Map<String, BigDecimal> calculateTaxes(long companyId, LocalDate startdate,
      LocalDate endDate) {
    final BigDecimal income = taxCalculatorService.calculateIncome(companyId, startdate, endDate);
    final BigDecimal costs = taxCalculatorService.calculateCost(companyId, startdate, endDate);
    Map<String, BigDecimal> taxesSummary = new LinkedHashMap<>();
    taxesSummary.put("Income", income);
    taxesSummary.put("Costs", costs);
    taxesSummary.put("Income - Costs", income.subtract(costs));
    taxesSummary.put("Pension Insurance", Rates.getPensionInsurance());
    taxesSummary.put("INCOME - COSTS - PENSION INSURANCE",
        income
            .subtract(costs)
            .subtract(Rates.getPensionInsurance()));
    BigDecimal taxBase = income
        .subtract(costs)
        .subtract(Rates.getPensionInsurance())
        .setScale(2, RoundingMode.UNNECESSARY);
    taxesSummary.put("Tax calculation base ", taxBase);
    if (companyService.findEntry(companyId).getTaxesType().equals(TaxesType.LINEAR)) {
      taxesSummary.putAll(calculateLinearIncomeTax(taxBase));
    } else {
      taxesSummary.putAll(calculateProgresiveIncomeTax(taxBase));
    }
    return taxesSummary;
  }

  private Map calculateLinearIncomeTax(BigDecimal taxBase) {
    Map<String, BigDecimal> linearTaxesSummary = new LinkedHashMap<>();
    BigDecimal inomeTax = taxBase
        .multiply(Rates.getLinearTaxRate());
    linearTaxesSummary.put(
        "Income tax (" + Rates.getLinearTaxRate().multiply(BigDecimal.valueOf(100)) + " % )"
        , inomeTax);
    linearTaxesSummary.put("Health insurance",
        inomeTax.multiply(Rates.getHealthInsuranceTaxRate()));
    linearTaxesSummary.put("INCOME TAX - HEALTH INSURANCE",
        inomeTax
            .subtract(inomeTax.multiply(Rates.getHealthInsuranceTaxRate())));
    linearTaxesSummary.put("Final income tax value",
        inomeTax
            .subtract(inomeTax.multiply(Rates.getHealthInsuranceTaxRate())))
        .setScale(2, RoundingMode.UNNECESSARY);
    return linearTaxesSummary;
  }

  private Map calculateProgresiveIncomeTax(BigDecimal taxBase) {

    return Collections.emptyMap();
  }

}