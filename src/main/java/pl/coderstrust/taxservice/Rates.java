package pl.coderstrust.taxservice;

import java.math.BigDecimal;

public class Rates {

  private static final BigDecimal LINEAR_TAX_RATE = BigDecimal.valueOf(0.19);
  private static final BigDecimal PROGRESSIVE_TAX_RATE_TRESHOLD_LOW_PERCENT =
      BigDecimal.valueOf(0.18);
  private static final BigDecimal PROGRESSIVE_TAX_RATE_TRESHOLD_HIGH_PERCENT =
      BigDecimal.valueOf(0.32);
  private static final BigDecimal PROGRESSIVE_TAX_RATE_TRESHOLD = BigDecimal.valueOf(85528);
  private static final BigDecimal HEALTH_INSURANCE_TAX_RATE = BigDecimal.valueOf(0.0775);
  private static final BigDecimal PENSION_INSURANCE = BigDecimal.valueOf(514.57);
  private static final BigDecimal DECREASING_TAX_AMOUNT = BigDecimal.valueOf(556.02);

  public static BigDecimal getHealthInsuranceTaxRate() {
    return HEALTH_INSURANCE_TAX_RATE;
  }

  public static BigDecimal getPensionInsurance() {
    return PENSION_INSURANCE;
  }

  public static BigDecimal getLinearTaxRate() {
    return LINEAR_TAX_RATE;
  }

  public static BigDecimal getProgressiveTaxRateTresholdLowPercent() {
    return PROGRESSIVE_TAX_RATE_TRESHOLD_LOW_PERCENT;
  }

  public static BigDecimal getProgressiveTaxRateTresholdHighPercent() {
    return PROGRESSIVE_TAX_RATE_TRESHOLD_HIGH_PERCENT;
  }

  public static BigDecimal getProgressiveTaxRateTreshold() {
    return PROGRESSIVE_TAX_RATE_TRESHOLD;
  }

  public static BigDecimal getDecreasingTaxAmount() {
    return DECREASING_TAX_AMOUNT;
  }


}