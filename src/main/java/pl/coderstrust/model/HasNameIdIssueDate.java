package pl.coderstrust.model;

import java.time.LocalDate;

public interface HasNameIdIssueDate {
  String getName();

  void setName(String name);

  long getId();

  void setId(long id);

  default LocalDate getIssueDate() {
    return LocalDate.now();
  }


}
