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
  private Invoice invoice = new Invoice();

  public InvoiceBuilder(long id, String buyerName, String sellerName) {
    invoice.setId(id);
    invoice.setBuyer(new CompanyBuilder(buyerName).createCompany());
    invoice.setSeller(new CompanyBuilder(sellerName).createCompany());
  }

  public InvoiceBuilder setVisibleId(String visibleId) {
    invoice.setVisibleId(visibleId);
    return this;
  }

  public InvoiceBuilder setBuyer(Company company) {
    invoice.setBuyer(company);
    return this;
  }

  public InvoiceBuilder setSeller(Company company) {
    invoice.setSeller(company);
    return this;
  }

  public InvoiceBuilder setIssueDate(LocalDate issueDate) {
    invoice.setIssueDate(issueDate);
    return this;
  }

  public InvoiceBuilder setPaymentDate(LocalDate paymentDate) {
    invoice.setPaymentDate(paymentDate);
    return this;
  }

  public InvoiceBuilder setProducts(List<InvoiceEntry> products) {
    invoice.setProducts(products);
    return this;
  }

  public InvoiceBuilder setPaymentState(PaymentState paymentState) {
    invoice.setPaymentState(paymentState);
    return this;
  }

  public Invoice createInvoice() {
    return invoice;
  }
}
