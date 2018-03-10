package pl.coderstrust.database.multifile;

import org.springframework.stereotype.Service;
import pl.coderstrust.model.HasNameIdIssueDate;

import java.io.File;

@Service
public class PathSelector {

  public String getFilePath(HasNameIdIssueDate invoice) {
    StringBuilder stringBuilder = new StringBuilder();
    String invoiceDateYear = String.valueOf(invoice.getIssueDate().getYear());
    String invoiceDateMonth = String.valueOf(invoice.getIssueDate().getMonth());
    String invoiceDateDay = String.valueOf(invoice.getIssueDate().getDayOfMonth());
    stringBuilder.append(Configuration.getJsonFilePath());
    stringBuilder.append(File.separator);
    stringBuilder.append(invoiceDateYear);
    stringBuilder.append(File.separator);
    stringBuilder.append(invoiceDateMonth);
    stringBuilder.append(File.separator);
    stringBuilder.append(invoiceDateDay);
    stringBuilder.append(".json");
    return stringBuilder.toString();
  }
}
