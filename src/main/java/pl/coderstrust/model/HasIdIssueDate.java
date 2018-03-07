package pl.coderstrust.model;

import java.time.LocalDate;

public interface HasIdIssueDate {

  long getId();

  void setId(long id);

  default LocalDate getIssueDate() {
    return LocalDate.now();
  }
}
