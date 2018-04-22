package pl.coderstrust.model;

//todo Possibly can make it inner class of Payment ,but maybe use in other classes.???
public enum PaymentType {

  PENSION_INSURANCE("Pension insurance"),
  HEALTH_INSURANCE("Health insurance"),
  INCOME_TAX_ADVANCE("Income tax advance");

  private String type;

  PaymentType(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }
}
