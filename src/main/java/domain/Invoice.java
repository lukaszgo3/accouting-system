package domain;

public class Invoice {

  private long idSeller;
  private long idBuyer;

  /**
   * Method returns sum.
   * @param firstNumber First number to sum.
   * @param secondNumber Second number to sum.
   */
  public int add(int firstNumber, int secondNumber) {
    return firstNumber + secondNumber;
  }
}
