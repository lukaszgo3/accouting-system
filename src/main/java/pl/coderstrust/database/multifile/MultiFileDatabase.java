package pl.coderstrust.database.multifile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import pl.coderstrust.database.Database;
import pl.coderstrust.database.database.memory.ObjectMapperProvider;
import pl.coderstrust.model.Invoice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MultiFileDatabase implements Database {

    private ObjectMapperProvider objectMapper;
    private FileHelper fileHelper;

    public MultiFileDatabase() {
        objectMapper = new ObjectMapperProvider();
        fileHelper = new FileHelper();
    }


  @Override
  public void addInvoice(Invoice invoice) {
    try {
      fileHelper.addLine(objectMapper.toJson(invoice), invoice);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void deleteInvoiceById(long id) {

  }

  @Override
  public Invoice getInvoiceById(long id) {

    return null;
  }

  @Override
  public void updateInvoice(Invoice invoice) {

  }

  @Override
  public List<Invoice> getInvoices() {
      List <Invoice> invoices = null;
      try {
          ArrayList<String> linesFromAllFiles = fileHelper.getAllFilesEntries();
          for (int i = 0; i <linesFromAllFiles.size()-1 ; i++) {
              invoices.add(objectMapper.toInvoice(linesFromAllFiles.get(i)));
          }
      } catch (IOException e) {
          e.printStackTrace();
      }
      return invoices;
  }

  private Invoice jsonToInvoice (String json){
      Invoice invoice = null;
      try {
         invoice= objectMapper.toInvoice(json);
      } catch (IOException e) {
          e.printStackTrace();
      }
      return invoice;
  }
}
