package domain;

import java.util.Date;

public class Invoice {

  private long id;
  private Date date;
  private long idSeller;
  private long idBuyer;

  /**
   * This is default constructor.
   */
  public Invoice() {
  }

  /**
   * This is constructor.
   * @param id unique invoice ID.
   * @param date invoice Data creation.
   * @param idSeller invoice seller unique ID.
   * @param idBuyer invoice buyer unique ID
   */
  public Invoice(long id, Date date, long idSeller, long idBuyer) {
    this.id = id;
    this.date = date;
    this.idSeller = idSeller;
    this.idBuyer = idBuyer;
  }

  /**
   * Method returns sum.
   * @param firstNumber First number to sum.
   * @param secondNumber Second number to sum.
   */
  public int add(int firstNumber, int secondNumber) {
    return firstNumber + secondNumber;
  }
}
