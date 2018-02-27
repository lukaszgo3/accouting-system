package pl.coderstrust.database.multifile;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import pl.coderstrust.database.Database;
import pl.coderstrust.database.DbException;
import pl.coderstrust.database.ExceptionMsg;
import pl.coderstrust.database.ObjectMapperHelper;
import pl.coderstrust.model.Invoice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
@Primary
public class MultiFileDatabase implements Database {

  private static final int FIRST_ID = 0;
  private static final int INCREMENT_ID = 1;

  private ObjectMapperHelper objectMapper;
  private FileHelper fileHelper;
  private Invoice invoice;
  private FileCache fileCache;
  private PathSelector pathSelector;

  public MultiFileDatabase() {
    objectMapper = new ObjectMapperHelper();
    fileHelper = new FileHelper();
    invoice = new Invoice();
    fileCache = new FileCache();
    pathSelector = new PathSelector();
  }


  @Override
  public long addInvoice(Invoice invoice) {
    invoice.setId(getNextId());
    fileHelper.addLine(objectMapper.toJson(invoice), invoice);
    fileCache.getCache().put(invoice.getId(), pathSelector.getFilePath(invoice));
    return invoice.getId();
  }

  private long getNextId() {
    if (fileCache.getCache().isEmpty()) {
      return FIRST_ID;
    } else {
      List keys = new ArrayList<>(fileCache.getCache().keySet());
      return (long) Collections.max(keys) + INCREMENT_ID;
    }
  }


  @Override
  public void deleteInvoice(long id) {
    if (!idExist(id)) {
      throw new DbException(ExceptionMsg.INVOICE_NOT_EXIST);
    } else {
      fileHelper.deleteLine(id);
      fileCache.getCache().remove(id);
    }
  }


  @Override
  public Invoice getInvoiceById(long id) {
    if (fileCache.getCache().containsKey(id)) {
      invoice = objectMapper.toInvoice(fileHelper.getLine(id));
    } else {
      throw new DbException(ExceptionMsg.INVOICE_NOT_EXIST);
    }
    return invoice;
  }

  @Override
  public void updateInvoice(Invoice invoice) {
    if (fileCache.getCache().containsKey(invoice.getId())) {
      deleteInvoice(invoice.getId());
      fileHelper.addLine(objectMapper.toJson(invoice), invoice);
      fileCache.getCache().put(invoice.getId(), pathSelector.getFilePath(invoice));
    }
  }


  @Override
  public List<Invoice> getInvoices() {

    List<Invoice> invoices = new ArrayList<>();
    ArrayList<String> linesFromAllFiles;
    linesFromAllFiles = fileHelper.getAllFilesEntries();
    for (String line : linesFromAllFiles) {
      invoices.add(objectMapper.toInvoice(line));
    }
    return invoices;
  }

  @Override
  public boolean idExist(long id) {
    boolean idCheck;
    if (fileCache.getCache().containsKey(id)) {
      idCheck = true;
    } else {
      idCheck = false;
    }
    return idCheck;
  }
}