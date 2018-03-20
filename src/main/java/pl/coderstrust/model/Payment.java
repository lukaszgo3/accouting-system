package pl.coderstrust.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Payment implements WithValidation {

  private long id;
  private LocalDate issueDate;
  private BigDecimal amount;
  private PaymentType type;

  public Payment(long id, LocalDate issueDate, BigDecimal amount,
      PaymentType type) {
    this.id = id;
    this.issueDate = issueDate;
    this.amount = amount;
    this.type = type;
  }

  public Payment() {
  }

  @Override
  public List<String> validate() {
    List<String> errors = new ArrayList<>();
    if (issueDate == null) {
      errors.add("Date is empty");
    }
    if (amount == null) {
      errors.add("Amount is empty");
    }
    if (type == null) {
      errors.add("Payment type is empty");
    }
    return errors;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public LocalDate getIssueDate() {
    return issueDate;
  }

  public void setIssueDate(LocalDate issueDate) {
    this.issueDate = issueDate;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public PaymentType getType() {
    return type;
  }

  public void setType(PaymentType type) {
    this.type = type;
  }
}


