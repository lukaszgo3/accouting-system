package pl.coderstrust.taxservice;

import java.math.BigDecimal;

public class Rates {

  private static BigDecimal linearTaxRate = BigDecimal.valueOf(0.19);
  private static BigDecimal progressiveTaxRateTresholdLowPercent = BigDecimal.valueOf(0.18);
  private static BigDecimal progressiveTaxRateTresholdHighPercent = BigDecimal.valueOf(0.32);
  private static BigDecimal progressiveTaxRateTreshold = BigDecimal.valueOf(85528);
  private static BigDecimal healthInsuranceTaxRate = BigDecimal.valueOf(0.0775);
  private static BigDecimal PENSION_INSURANCE = BigDecimal.valueOf(514.57);

  public static BigDecimal getHealthInsuranceTaxRate() {
    return healthInsuranceTaxRate;
  }

  public void setHealthInsuranceTaxRate(BigDecimal healthInsuranceTaxRate) {
    this.healthInsuranceTaxRate = healthInsuranceTaxRate;
  }

  public static BigDecimal getPensionInsurance() {
    return PENSION_INSURANCE;
  }

  public static BigDecimal getLinearTaxRate() {
    return linearTaxRate;
  }

  public void setLinearTaxRate(BigDecimal linearTaxRate) {
    this.linearTaxRate = linearTaxRate;
  }

  public static BigDecimal getProgressiveTaxRateTresholdLowPercent() {
    return progressiveTaxRateTresholdLowPercent;
  }

  public void setProgressiveTaxRateTresholdLowPercent(
      BigDecimal progressiveTaxRateTresholdLowPercent) {
    this.progressiveTaxRateTresholdLowPercent = progressiveTaxRateTresholdLowPercent;
  }

  public static BigDecimal getProgressiveTaxRateTresholdHighPercent() {
    return progressiveTaxRateTresholdHighPercent;
  }

  public void setProgressiveTaxRateTresholdHighPercent(
      BigDecimal progressiveTaxRateTresholdHighPercent) {
    this.progressiveTaxRateTresholdHighPercent = progressiveTaxRateTresholdHighPercent;
  }

  public static BigDecimal getProgressiveTaxRateTreshold() {
    return progressiveTaxRateTreshold;
  }

  public void setProgressiveTaxRateTreshold(BigDecimal progressiveTaxRateTreshold) {
    this.progressiveTaxRateTreshold = progressiveTaxRateTreshold;
  }
}