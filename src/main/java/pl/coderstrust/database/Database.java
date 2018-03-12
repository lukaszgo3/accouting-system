package pl.coderstrust.database;

import pl.coderstrust.model.withNameIdIssueDate;

import java.util.List;

public interface Database<T extends withNameIdIssueDate> {

  long addEntry(T entry);

  void deleteEntry(long id);

  T getEntryById(long id);

  void updateEntry(T entry);

  List<T> getEntries();

  boolean idExist(long id);

}
