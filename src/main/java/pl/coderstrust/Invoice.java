package pl.coderstrust;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Invoice {

  private Id id;
  private Buyer buyer;
  private Seller seller;
  private static final LocalDate ISSUE_DATE = LocalDate.now();
  private static final LocalDate PAYMENT_DATE = ISSUE_DATE.plusDays(14);
  List<InvoiceEntry> products = new ArrayList<>();
  private double totalCost;
  private double totalVatCost;
  private double totalNetcost;
  private PaymentState paymentState;

  /**
   * Method returns sum.
   *
   * @param firstNumber First number to sum.
   * @param secondNumber Second number to sum.
   */
  public int add(int firstNumber, int secondNumber) {
    return firstNumber + secondNumber;
  }
}
