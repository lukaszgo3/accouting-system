package pl.coderstrust.database;

import java.util.List;
import pl.coderstrust.model.WithNameIdIssueDate;

public interface Database<T extends WithNameIdIssueDate> {

  long addEntry(T entry);

  void deleteEntry(long id);

  T getEntryById(long id);

  void updateEntry(T entry);

  List<T> getEntries();

  boolean idExist(long id);

}
