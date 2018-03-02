package pl.coderstrust.e2e.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public enum PaymentState {
  PAID("Paid"),
  NOT_PAID("Not paid");

  private final String state;

  PaymentState(String state) {
    this.state = state;
  }
}
