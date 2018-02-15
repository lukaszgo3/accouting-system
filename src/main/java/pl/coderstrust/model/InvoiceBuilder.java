package pl.coderstrust.model;

import java.time.LocalDate;
import java.util.List;

public class InvoiceBuilder {

  private long id;
  private String visibleId;
  private Company buyer;
  private Company seller;
  private LocalDate issueDate;
  private LocalDate paymentDate;
  private List<InvoiceEntry> products;
  private PaymentState paymentState;

  public InvoiceBuilder(long id, String buyerName, String sellerName) {
    this.id = id;
    this.buyer = new CompanyBuilder(buyerName).createCompany();
    this.seller = new CompanyBuilder(sellerName).createCompany();
  }

  public InvoiceBuilder setVisibleId(String visibleId) {
    this.visibleId = visibleId;
    return this;
  }

  public InvoiceBuilder setBuyer(Company company) {
    this.buyer = company;
    return this;
  }

  public InvoiceBuilder setSeller(Company company) {
    this.buyer = company;
    return this;
  }

  public InvoiceBuilder setIssueDate(LocalDate issueDate) {
    this.issueDate = issueDate;
    return this;
  }

  public InvoiceBuilder setPaymentDate(LocalDate paymentDate) {
    this.paymentDate = paymentDate;
    return this;
  }

  public InvoiceBuilder setProducts(List<InvoiceEntry> products) {
    this.products = products;
    return this;
  }

  public InvoiceBuilder setPaymentState(PaymentState paymentState) {
    this.paymentState = paymentState;
    return this;
  }

  public Invoice createInvoice() {
    Invoice invoice = new Invoice();
    invoice.setId(this.id);
    invoice.setVisibleId(this.visibleId);
    invoice.setBuyer(this.buyer);
    invoice.setSeller(this.seller);
    invoice.setIssueDate(this.issueDate);
    invoice.setPaymentDate(this.paymentDate);
    invoice.setProducts(this.products);
    invoice.setPaymentState(this.paymentState);
    return invoice;
  }
}
