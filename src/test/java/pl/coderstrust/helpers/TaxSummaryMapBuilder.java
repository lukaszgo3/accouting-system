package pl.coderstrust.helpers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.Map;

public class TaxSummaryMapBuilder {

  private LinkedHashMap<String, BigDecimal> map = new LinkedHashMap<>();

  public TaxSummaryMapBuilder setIncome(double income) {
    map.put("Income", BigDecimal.valueOf(income).setScale(2, RoundingMode.CEILING));
    return this;
  }

  public TaxSummaryMapBuilder setCosts(double costs) {
    map.put("Costs", BigDecimal.valueOf(costs).setScale(2, RoundingMode.CEILING));
    return this;
  }

  public TaxSummaryMapBuilder setIncomeMinusCosts(double incomeMinusCosts) {
    map.put("Income - Costs",
        BigDecimal.valueOf(incomeMinusCosts).setScale(2, RoundingMode.CEILING));
    return this;
  }

  public TaxSummaryMapBuilder setPensionInsuranceMonthlyRate(double pensionInsuranceRate) {
    map.put("Pension Insurance monthly rate",
        BigDecimal.valueOf(pensionInsuranceRate).setScale(2, RoundingMode.CEILING));
    return this;
  }

  public TaxSummaryMapBuilder setPensionInsurancePaid(double pensionInsurancePaid) {
    map.put("Pension insurance paid",
        BigDecimal.valueOf(pensionInsurancePaid).setScale(2, RoundingMode.CEILING));
    return this;
  }

  public TaxSummaryMapBuilder setTaxCalculationBase(double taxCalculationBase) {
    map.put("Tax calculation base",
        BigDecimal.valueOf(taxCalculationBase).setScale(2, RoundingMode.CEILING));
    return this;
  }

  public TaxSummaryMapBuilder setIncomeTax(double incomeTax) {
    map.put("Income tax", BigDecimal.valueOf(incomeTax).setScale(2, RoundingMode.CEILING));
    return this;
  }

  public TaxSummaryMapBuilder setDecreasingTaxAmount(double decreasingTaxAmount) {
    map.put("Decreasing tax amount",
        BigDecimal.valueOf(decreasingTaxAmount).setScale(2, RoundingMode.CEILING));
    return this;
  }

  public TaxSummaryMapBuilder setIncomeTaxPaid(double incomeTaxPaid) {
    map.put("Income tax paid", BigDecimal.valueOf(incomeTaxPaid).setScale(2, RoundingMode.CEILING));
    return this;
  }

  public TaxSummaryMapBuilder setHealthInsurancePaid(double healthInsurancePaid) {
    map.put("Health insurance paid",
        BigDecimal.valueOf(healthInsurancePaid).setScale(2, RoundingMode.CEILING));
    return this;
  }

  public TaxSummaryMapBuilder setHealthInsuranceSubtract(double healthInsuranceSubtract) {
    map.put("Health insurance to subtract",
        BigDecimal.valueOf(healthInsuranceSubtract).setScale(2, RoundingMode.CEILING));
    return this;
  }

  public TaxSummaryMapBuilder setIncomeTaxToPay(double incomeTaxToPay) {
    map.put("Income tax - health insurance to subtract - income tax paid",
        BigDecimal.valueOf(incomeTaxToPay).setScale(2, RoundingMode.CEILING));
    return this;
  }

  public Map<String, BigDecimal> build() {
    return map;
  }
}