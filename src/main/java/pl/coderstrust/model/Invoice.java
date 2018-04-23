package pl.coderstrust.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement
@XmlType(name = "model.Invoice", propOrder = {
    "id",
    "name",
    "buyer",
    "seller",
    "issueDate",
    "paymentDate",
    "paymentState",
    "products"}, namespace = "https://coderstrust.pl/invoice")
public class Invoice implements WithNameIdIssueDate, WithValidation {

  private List<InvoiceEntry> products = new ArrayList<>();
  private long id;
  private String name;
  private Company buyer;
  private Company seller;
  private LocalDate issueDate;
  private LocalDate paymentDate;
  private PaymentState paymentState;


  public Invoice() {
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
  }

  @JsonProperty("invoiceId")
  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  @ApiModelProperty(example = "FV 2/22/06/2019")
  @XmlElement(required = true)
  public String getName() {
    return name;
  }

  public void setName(String invoiceName) {
    this.name = invoiceName;
  }

  @XmlElement(required = true)
  public Company getBuyer() {
    return buyer;
  }

  public void setBuyer(Company buyer) {
    this.buyer = buyer;
  }

  @XmlElement(required = true)
  public Company getSeller() {
    return seller;
  }

  public void setSeller(Company seller) {
    this.seller = seller;
  }

  @ApiModelProperty(example = "2019-06-15")
  @XmlElement(name = "invoiceIssueDate", required = true)
  @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
  public LocalDate getIssueDate() {
    return issueDate;
  }

  public void setIssueDate(String issueDate) {
    this.issueDate = LocalDate.parse(issueDate);
  }

  public void setIssueDate(LocalDate issueDate) {
    this.issueDate = issueDate;
  }

  @ApiModelProperty(example = "2019-07-15")
  @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
  @XmlElement(required = true)
  public LocalDate getPaymentDate() {
    return paymentDate;
  }

  public void setPaymentDate(String paymentDate) {
    this.paymentDate = LocalDate.parse(paymentDate);
  }

  public void setPaymentDate(LocalDate paymentDate) {
    this.paymentDate = paymentDate;
  }

  @XmlElement(required = true)
  public List<InvoiceEntry> getProducts() {
    return products;
  }

  public void setProducts(List<InvoiceEntry> products) {
    this.products = products;
  }

  public PaymentState getPaymentState() {
    return paymentState;
  }

  @XmlElement(required = true)
  public void setPaymentState(PaymentState paymentState) {
    this.paymentState = paymentState;
  }

  @Override
  public List<String> validate() {
    List<String> errors = new ArrayList<>();
    errors.addAll(this.getSeller().validate());
    errors.addAll(this.getBuyer().validate());
    errors.addAll(checkDate(this.getIssueDate()));
    errors.addAll(checkDate(this.getPaymentDate()));
    if (this.getProducts().size() == 0) {
      errors.add(Messages.PRODUCTS_LIST_EMPTY);
    } else {
      for (int i = 0; i < this.getProducts().size(); i++) {
        if (this.getProducts().get(i).getAmount() <= 0) {
          errors.add(Messages.PRODUCT_INCORRECT_AMOUNT);
        }
        errors.addAll(this.getProducts().get(i).getProduct().validate());
      }
    }
    if (this.getPaymentState() == null) {
      errors.add(Messages.PAYMENT_STATE_EMPTY);
    }
    return errors;
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
