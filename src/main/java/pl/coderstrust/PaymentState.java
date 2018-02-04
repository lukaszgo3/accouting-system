package pl.coderstrust;

public enum PaymentState {
    PAID("Paid"),
    NOTPAID("Not paid");

    private final String state;

    PaymentState(String state) {
        this.state = state;
    }
}
