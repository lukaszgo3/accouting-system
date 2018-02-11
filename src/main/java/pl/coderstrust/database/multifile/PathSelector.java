package pl.coderstrust.database.multifile;

import com.fasterxml.jackson.databind.ObjectMapper;

import pl.coderstrust.model.Invoice;

public class PathSelector {

  ObjectMapper objectMapper = new ObjectMapper();
  StringBuilder stringBuilder = new StringBuilder();

  String filePath = "";

  public PathSelector(String filePath) {
    this.filePath = filePath;
  }

  /**
   * PathSelector.
   * gives specific path of each invoice
   * to write and read on disk
   */

  public String getFilePath(Invoice invoice) {
    String invoiceDateYear = String.valueOf(invoice.getIssueDate().getDayOfYear());
    String invoiceDateMonth = String.valueOf(invoice.getIssueDate().getDayOfYear());
    String invoiceDateDay = String.valueOf(invoice.getIssueDate().getDayOfYear());

    stringBuilder.append(invoiceDateYear);
    stringBuilder.append("\\");
    stringBuilder.append(invoiceDateMonth);
    stringBuilder.append("\\");
    stringBuilder.append(invoiceDateDay);
    stringBuilder.append("\\");
    stringBuilder.append(invoiceDateYear + invoiceDateMonth + invoiceDateDay + ".json");

    filePath = stringBuilder.toString();
    return filePath;
  }
}
