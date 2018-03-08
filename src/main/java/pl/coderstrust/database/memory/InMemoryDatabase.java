package pl.coderstrust.database.memory;

import pl.coderstrust.database.Database;
import pl.coderstrust.database.DbException;
import pl.coderstrust.database.ExceptionMsg;
import pl.coderstrust.model.HasIdIssueDate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//@Repository //TODO: skasowac
//@ConditionalOnProperty(name = "pl.coderstrust.database.Database", havingValue = "inMemory")
public class InMemoryDatabase<T> implements Database {

  private static final int INITIAL_ID = 0;
  private static final int INCREMENT_ID = 1;

  private HashMap<Long, HasIdIssueDate> invoices = new HashMap<>();
  private long lastId = INITIAL_ID;


  public InMemoryDatabase(Class <T> entryClass){
  }

  @Override
  public long addEntry(HasIdIssueDate entry) {
    entry.setId(getNextId());
    invoices.put(entry.getId(), entry);
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

    invoices.remove(id);
  }

  @Override
  public HasIdIssueDate getEntryById(long id) {
    if (!idExist(id)) {
      throw new DbException(ExceptionMsg.INVOICE_NOT_EXIST);
      //TODO add logging.
    }
    return invoices.get(id);
  }

  @Override
  public void updateEntry(HasIdIssueDate entry) {
    if (!idExist(entry.getId())) {
      throw new DbException(ExceptionMsg.INVOICE_NOT_EXIST);
      //TODO add logging.
    }

    invoices.replace(entry.getId(), entry);
  }

  @Override
  public List<HasIdIssueDate> getEntries() {
    return new ArrayList<>(invoices.values());
  }

  @Override
  public boolean idExist(long id) {
    return invoices.containsKey(id);
  }

}