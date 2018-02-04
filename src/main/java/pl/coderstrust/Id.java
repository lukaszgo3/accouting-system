package pl.coderstrust;

import java.time.LocalDate;

public class Id {

  private final String id; //id= number + date ???
  private int number;
  private LocalDate date = LocalDate.now();

  public Id() {
    this.id = "Number + Date";
    this.number = getNumber();
  }

  private static int getNumber() {
    return 0;
  }
}
