package pl.coderstrust.e2e.model;

public enum ProductType {
  CAR("Car"),
  OFFICE("Office"),
  ELECTRIONICS("Electronics"),
  CLEANERS("Cleaners"),
  OTHER("Other");

  private String type;

  ProductType(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }
}