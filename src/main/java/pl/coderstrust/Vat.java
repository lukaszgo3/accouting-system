package pl.coderstrust;

public enum Vat {
  vat23(0.23),
  vat7(0.07),
  vat0(0);

  private final double vatPercent;

  Vat(double vatPercent) {
    this.vatPercent = vatPercent;
  }

  private double getVatPercent() {
    return vatPercent;
  }
}
