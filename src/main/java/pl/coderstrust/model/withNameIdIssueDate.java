package pl.coderstrust.model;

import java.time.LocalDate;

public interface withNameIdIssueDate {

  String getName();

  void setName(String name);

  long getId();

  void setId(long id);

  LocalDate getIssueDate();

  void setIssueDate(LocalDate issueDate);
}
