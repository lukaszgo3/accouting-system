package pl.coderstrust.service;

import pl.coderstrust.database.Database;
import pl.coderstrust.model.HasNameIdIssueDate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class Book<T extends HasNameIdIssueDate> {

  protected static final LocalDate MIN_DATE = LocalDate.of(1500, 11, 12);
  protected static final LocalDate MAX_DATE = LocalDate.of(3000, 11, 12);

  Database<T> database;

  public long addEntry(T entry) {
    setDefaultEntryNameIfEmpty(entry);
    return database.addEntry(entry);
  }

  public void deleteEntry(long id) {
    database.deleteEntry(id);
  }

  public T findEntry(long id) {
    return (T) database.getEntryById(id);
  }

  public List<T> getEntry() {
    return database.getEntries();
  }

  public boolean idExist(long id) {
    return database.idExist(id);
  }

  public void updateEntry(T entry) {
    setDefaultEntryNameIfEmpty(entry);
    database.updateEntry(entry);
  }

  public List<T> getEntryByDate(LocalDate beginDate, LocalDate endDate) {
    if (beginDate == null) {
      beginDate = MIN_DATE;
    }
    if (endDate == null) {
      endDate = MAX_DATE;
    }
    List<T> selectedEntries = new ArrayList<>();
    List<T> allEntries = database.getEntries();
    for (T entry : allEntries) {
      if (entry.getIssueDate().isBefore(endDate.plusDays(1)) && entry.getIssueDate()
          .isAfter(beginDate.minusDays(1))) {
        selectedEntries.add(entry);
      }
    }
    return selectedEntries;
  }

  public List<T> getEntries() {
    return database.getEntries();
  }

  protected void setDefaultEntryNameIfEmpty(T entry) {
    if (entry.getName() == null || entry.getName().trim().length() == 0) {
      entry.setName(String.format("%d / %s", entry.getId(), entry.getIssueDate()));
    }
  }
}