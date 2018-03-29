package pl.coderstrust.e2e.model;

public enum TaxType {
  LINEAR("LINEAR"),
  PROGRESIVE("Progresive");

  private String taxesType;

  TaxType(String taxesType) {
    this.taxesType = taxesType;
  }

  public String getTaxesType() {
    return taxesType;
  }
}