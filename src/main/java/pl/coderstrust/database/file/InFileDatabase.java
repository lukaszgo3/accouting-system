package pl.coderstrust.database.file;

import pl.coderstrust.database.Database;
import pl.coderstrust.database.ObjectMapperHelper;
import pl.coderstrust.model.Invoice;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Collectors;

public class InFileDatabase implements Database {

  private FileHelper fileHelper;
  private ObjectMapperHelper mapper;
  private HashSet<Long> savedIds;

  public InFileDatabase() {
    mapper = new ObjectMapperHelper();
    fileHelper = new FileHelper();
    savedIds = getSavedIds();
  }

  @Override
  public void addInvoice(Invoice invoice) {
    fileHelper.addLine(mapper.toJson(invoice));
    savedIds.add(invoice.getId());
  }

  @Override
  public void deleteInvoice(long systemId) {
    fileHelper.deleteLine(idToLineKey(systemId));
    savedIds.remove(systemId);
  }

  @Override
  public Invoice getInvoiceById(long systemId) {
    String jsonInvoice = fileHelper.getLine(idToLineKey(systemId));
    return mapper.toInvoice(jsonInvoice);
  }

  String idToLineKey(long systemId) {
    return "\"id\":" + String.valueOf(systemId) + ",";
  }

  @Override
  public void updateInvoice(Invoice invoice) {
    deleteInvoice(invoice.getId());
    addInvoice(invoice);
  }

  @Override
  public ArrayList<Invoice> getInvoices() {
    return fileHelper.getAllLines().stream()
        .map(line -> mapper.toInvoice(line))
        .collect(Collectors.toCollection(ArrayList::new));
  }

  @Override
  public boolean idExist(long id) {
    return savedIds.contains(id);
  }

  private HashSet<Long> getSavedIds() {
    return getInvoices().stream()
        .map(invoice -> invoice.getId())
        .collect(Collectors.toCollection(HashSet::new));
  }
}