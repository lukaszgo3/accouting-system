package pl.coderstrust.model;

import java.math.BigDecimal;

public class ProductBuilder {
  private String name;
  private String description;
  private BigDecimal netValue;
  private Vat vatRate;

  /**
   * If not provided Vat rate is set to 23%.
   *
   * @param name     -Product name.
   * @param netValue = Product net value.
   */
  public ProductBuilder(String name, double netValue) {
    this.name = name;
    this.netValue = BigDecimal.valueOf(netValue);
    this.vatRate = Vat.VAT_23;
  }

  public ProductBuilder setDescription(String description) {
    this.description = description;
    return this;
  }

  public ProductBuilder setVatRate(Vat vatRate) {
    this.vatRate = vatRate;
    return this;
  }

  public Product createProduct() {
    Product product = new Product();
    product.setName(this.name);
    product.setNetValue(this.netValue);
    product.setVatRate(this.vatRate);
    product.setDescription(this.description);
    return product;
  }
}