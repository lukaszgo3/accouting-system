package pl.coderstrust.model;

import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Invoice {

  private long id;
  private String invoiceName;
  private Company buyer;
  private Company seller;
  private LocalDate issueDate;
  private LocalDate paymentDate;
  List<InvoiceEntry> products = new ArrayList<>();
  private PaymentState paymentState;

  public Invoice() {
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  @ApiModelProperty(example = "FV 2/22/06/2019")
  public String getInvoiceName() {
    return invoiceName;
  }

  public void setInvoiceName(String invoiceName) {
    this.invoiceName = invoiceName;
  }

  public Company getBuyer() {
    return buyer;
  }

  public void setBuyer(Company buyer) {
    this.buyer = buyer;
  }

  public Company getSeller() {
    return seller;
  }

  public void setSeller(Company seller) {
    this.seller = seller;
  }

  @ApiModelProperty(example = "2019-06-15")
  public LocalDate getIssueDate() {
    return issueDate;
  }

  public void setIssueDate(LocalDate issueDate) {
    this.issueDate = issueDate;
  }

  public void setIssueDate(String issueDate) {
    this.issueDate = LocalDate.parse(issueDate);
  }

  @ApiModelProperty(example = "2019-07-15")
  public LocalDate getPaymentDate() {
    return paymentDate;
  }

  public void setPaymentDate(LocalDate paymentDate) {
    this.paymentDate = paymentDate;
  }

  public void setPaymentDate(String paymentDate) {
    this.paymentDate = LocalDate.parse(paymentDate);
  }

  public List<InvoiceEntry> getProducts() {
    return products;
  }

  public void setProducts(List<InvoiceEntry> products) {
    this.products = products;
  }

  public PaymentState getPaymentState() {
    return paymentState;
  }

  public void setPaymentState(PaymentState paymentState) {
    this.paymentState = paymentState;
  }

  @Override
  public boolean equals(Object object) {
    return EqualsBuilder.reflectionEquals(this, object);
  }

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this, true);
  }
}
