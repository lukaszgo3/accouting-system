package pl.coderstrust.model;

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