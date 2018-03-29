package pl.coderstrust.e2e.invalidInputTest;

import pl.coderstrust.e2e.model.Company;
import pl.coderstrust.e2e.model.Invoice;

public class InvalidInputTestsV1 extends AbstractInvalidInputTests {

  private String basePath = "v1/invoice";

  @Override
  protected String getBasePath() {
    return basePath + "/";
  }

  @Override
  Invoice getDefaultTestInvoice() {
    return generator
        .getTestInvoice(config.getDefaultTestInvoiceNumber(), config.getDefaultEntriesCount());
  }

  @Override
  Company getDefaultTestCompany() {
    return generator.getTestCompany(config.getDefaultTestInvoiceNumber(), "company");
  }


}
