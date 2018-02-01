import java.util.Date;

public class Invoice {

  private long id;
  private Date date;
  private long idSeller;
  private long idBuyer;

  /**
   *
   * @param id unique invoice ID.
   * @param date  invoice Data creation.
   * @param idSeller invoice seller unique ID.
   * @param idBuyer invoice buyer unique ID
   */
  public Invoice(long id, Date date, long idSeller, long idBuyer) {
    this.id = id;
    this.date = date;
    this.idSeller = idSeller;
    this.idBuyer = idBuyer;
  }
}
