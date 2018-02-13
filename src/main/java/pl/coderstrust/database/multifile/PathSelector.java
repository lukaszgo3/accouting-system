package pl.coderstrust.database.multifile;

import pl.coderstrust.model.Invoice;

import java.io.File;

public class PathSelector {

  private StringBuilder stringBuilder = new StringBuilder();

  String filePath = "";
  private Invoice invoice;

  public PathSelector(Invoice invoice) {
    this.invoice = invoice;
  }

  public PathSelector() {
  }

  /**
   * PathSelector. gives specific path of each invoice to write and read on disk
   */

  public String getFilePath(Invoice invoice) {
    String invoiceDateYear = String.valueOf(invoice.getIssueDate().getYear());
    String invoiceDateMonth = String.valueOf(invoice.getIssueDate().getMonth());
    String invoiceDateDay = String.valueOf(invoice.getIssueDate().getDayOfMonth());

    stringBuilder.append("database");
    stringBuilder.append(File.separator);
    stringBuilder.append(invoiceDateYear);
    stringBuilder.append(File.separator);
    stringBuilder.append(invoiceDateMonth);
    stringBuilder.append(File.separator);
    stringBuilder.append(invoiceDateDay);
    stringBuilder.append(".json");

    filePath = stringBuilder.toString();
    return filePath;
  }
}
