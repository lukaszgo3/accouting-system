package pl.coderstrust.model;

public enum ProductType {
  CAR("CAR"),
  NO_CAR("NO_CAR");

  private String type;

  ProductType(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }
}