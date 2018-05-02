package pl.coderstrust.model;

import java.time.LocalDateTime;

public class Token {

  private String number;
  private LocalDateTime dateTime;

  public Token(String number, LocalDateTime dateTime) {
    this.number = number;
    this.dateTime = dateTime;
  }

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public LocalDateTime getDateTime() {
    return dateTime;
  }

  public void setDateTime(LocalDateTime dateTime) {
    this.dateTime = dateTime;
  }
}
