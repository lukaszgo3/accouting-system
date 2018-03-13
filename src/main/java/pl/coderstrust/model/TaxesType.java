package pl.coderstrust.model;

public enum TaxesType {
  LINEAR("LINEAR"),
  PROGRESIVE("Progresive");

  private String taxesType;

  TaxesType(String taxesType) {
    this.taxesType = taxesType;
  }

  public String getTaxesType() {
    return taxesType;
  }
}