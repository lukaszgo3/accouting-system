package pl.coderstrust.database.multifile;

import pl.coderstrust.database.Database;
import pl.coderstrust.database.DbException;
import pl.coderstrust.database.ExceptionMsg;
import pl.coderstrust.database.ObjectMapperHelper;
import pl.coderstrust.model.HasNameIdIssueDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//@Repository
//@ConditionalOnProperty(name = "pl.coderstrust.database.Database", havingValue = "multiFile")
public class MultiFileDatabase<T extends HasNameIdIssueDate> implements Database<T> {

  private static final int FIRST_ID = 0;
  private static final int INCREMENT_ID = 1;

  private ObjectMapperHelper objectMapper;
  private FileHelper fileHelper;
  private FileCache fileCache;
  private PathSelector pathSelector;

  public MultiFileDatabase(Class<T> entryClass) {
    objectMapper = new ObjectMapperHelper(entryClass);
    fileCache = new FileCache(objectMapper);
    pathSelector = new PathSelector();
    fileHelper = new FileHelper(fileCache, pathSelector);
  }

  @Override
  public long addEntry(T entry) {
    entry.setId(getNextId());
    fileHelper.addLine(objectMapper.toJson(entry), entry);
    fileCache.getCache().put(entry.getId(), pathSelector.getFilePath(entry));
    return entry.getId();
  }

  private long getNextId() {
    return fileCache.getCache().isEmpty() ? FIRST_ID :
        (long) Collections.max(fileCache.getCache().keySet()) + INCREMENT_ID;
  }

  @Override
  public void deleteEntry(long id) {
    if (!idExist(id)) {
      throw new DbException(ExceptionMsg.INVOICE_NOT_EXIST);
    } else {
      fileHelper.deleteLine(id);
      fileCache.getCache().remove(id);
    }
  }

  @Override
  public T getEntryById(long id) {
    T invoice;
    if (fileCache.getCache().containsKey(id)) {
      invoice = (T) objectMapper.toObject(fileHelper.getLine(id));
    } else {
      throw new DbException(ExceptionMsg.INVOICE_NOT_EXIST);
    }
    return invoice;
  }

  @Override
  public void updateEntry(T entry) {
    if (fileCache.getCache().containsKey(entry.getId())) {
      deleteEntry(entry.getId());
      fileHelper.addLine(objectMapper.toJson(entry), entry);
      fileCache.getCache().put(entry.getId(), pathSelector.getFilePath(entry));
    }
  }

  @Override
  public List<T> getEntries() {
    List<T> invoices = new ArrayList<>();
    ArrayList<String> linesFromAllFiles;
    linesFromAllFiles = fileCache.getAllFilesEntries();
    for (String line : linesFromAllFiles) {
      invoices.add((T) objectMapper.toObject(line));
    }
    return invoices;
  }

  @Override
  public boolean idExist(long id) {
    return fileCache.getCache().keySet().contains(id);
  }
}