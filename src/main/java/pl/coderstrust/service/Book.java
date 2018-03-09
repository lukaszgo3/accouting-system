package pl.coderstrust.service;

import pl.coderstrust.database.Database;
import pl.coderstrust.model.HasNameIdIssueDate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class Book<T extends HasNameIdIssueDate> {

  protected static final LocalDate MIN_DATE = LocalDate.of(1500, 11, 12);
  protected static final LocalDate MAX_DATE = LocalDate.of(3000, 11, 12);

  Database dbInstance;

  public long addEntry(T entry) {
    setDefaultEntryNameIfEmpty(entry);
    return dbInstance.addEntry(entry);
  }

  public void deleteInvoice(long id) {
    dbInstance.deleteEntry(id);
  }

  public T findInvoice(long id) {
    return (T) dbInstance.getEntryById(id);
  }

  public List<T> getInvoices() {
    return dbInstance.getEntries();
  }

  public boolean idExist(long id) {
    return dbInstance.idExist(id);
  }

  public void updateInvoice(T entry) {
    setDefaultEntryNameIfEmpty(entry);
    dbInstance.updateEntry(entry);
  }

  public List<T> getInvoiceByDate(LocalDate beginDate, LocalDate endDate) {
    if (beginDate == null) {
      beginDate = MIN_DATE;
    }
    if (endDate == null) {
      endDate = MAX_DATE;
    }
    List<T> selectedEntries = new ArrayList<>();
    List<T> allEntries = dbInstance.getEntries();
    for (T entry : allEntries) {
      if (entry.getIssueDate().isBefore(endDate.plusDays(1)) && entry.getIssueDate()
          .isAfter(beginDate.minusDays(1))) {
        selectedEntries.add(entry);
      }
    }
    return selectedEntries;
  }

  protected void setDefaultEntryNameIfEmpty(T entry) {
    if (entry.getName() == null || entry.getName().trim().length() == 0) {
      entry.setName(String.format("%d / %s", entry.getId(), entry.getIssueDate()));
    }
  }

}
