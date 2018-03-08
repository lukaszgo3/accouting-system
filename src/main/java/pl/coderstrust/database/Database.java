package pl.coderstrust.database;

import pl.coderstrust.model.HasIdIssueDate;

import java.util.List;

public interface Database<T> {

  long addEntry(HasIdIssueDate entry);

  void deleteEntry(long id);

  T getEntryById(long id);

  void updateEntry(HasIdIssueDate entry);

  List<T> getEntries();

  boolean idExist(long id);

}
