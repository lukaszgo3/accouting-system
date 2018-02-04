package pl.coderstrust;

public enum VAT {
    VAT23(0.23),
    VAT7(0.07),
    VAT0(0);

    private final double vatPercent;

    VAT(double vatPercent) {
        this.vatPercent = vatPercent;
    }

    public double getVatPercent() {
        return vatPercent;
    }
}
