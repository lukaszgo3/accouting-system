package pl.coderstrust.database.memory;

import pl.coderstrust.database.Database;
import pl.coderstrust.database.DbException;
import pl.coderstrust.database.ExceptionMsg;
import pl.coderstrust.model.HasNameIdIssueDate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//@Repository //TODO: skasowac
//@ConditionalOnProperty(name = "pl.coderstrust.database.Database", havingValue = "inMemory")
public class InMemoryDatabase<T extends HasNameIdIssueDate> implements Database<T> {

  private static final int INITIAL_ID = 0;
  private static final int INCREMENT_ID = 1;

  private HashMap<Long, T> entries = new HashMap<>();
  private long lastId = INITIAL_ID;


  public InMemoryDatabase(Class<T> entryClass) {
  }

  @Override
  public long addEntry(T entry) {
    entry.setId(getNextId());
    entries.put(entry.getId(), entry);
    return entry.getId();
  }

  private long getNextId() {
    lastId += INCREMENT_ID;
    return lastId;
  }

  @Override
  public void deleteEntry(long id) {
    if (!idExist(id)) {
      throw new DbException(ExceptionMsg.INVOICE_NOT_EXIST);
      //TODO add logging.
    }

    entries.remove(id);
  }

  @Override
  public T getEntryById(long id) {
    if (!idExist(id)) {
      throw new DbException(ExceptionMsg.INVOICE_NOT_EXIST);
      //TODO add logging.
    }
    return entries.get(id);
  }

  @Override
  public void updateEntry(T entry) {
    if (!idExist(entry.getId())) {
      throw new DbException(ExceptionMsg.INVOICE_NOT_EXIST);
      //TODO add logging.
    }

    entries.replace(entry.getId(), entry);
  }

  @Override
  public List<T> getEntries() {
    return new ArrayList<>(entries.values());
  }

  @Override
  public boolean idExist(long id) {
    return entries.containsKey(id);
  }

}