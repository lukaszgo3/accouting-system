package pl.coderstrust.database;

import pl.coderstrust.model.HasUniqueId;

import java.util.List;

public interface Database {

  long addEntry(HasUniqueId entry);

  void deleteEntry(long id);

  Object getEntryById(long id);

  void updateEntry(HasUniqueId entry);

  List<HasUniqueId> getEntries();

  boolean idExist(long id);
}
