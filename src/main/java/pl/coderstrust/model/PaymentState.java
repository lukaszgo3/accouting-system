package pl.coderstrust.model;

public enum PaymentState {
  PAID("Paid"),
  NOT_PAID("Not paid");

  PaymentState() {
  }

  private String state;

  PaymentState(String state) {
    this.state = state;
  }

  public String getState() {
    return state;
  }
}
