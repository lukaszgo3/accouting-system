package pl.coderstrust.e2e.invalidInputTest;

import pl.coderstrust.e2e.model.Company;
import pl.coderstrust.e2e.model.Invoice;

public class InvalidInputTestByV2 extends AbstractInvalidInputTests {

  private String basePath = "v2/company";

  @Override
  protected String getBasePath() {
    return basePath + "/";
  }

  @Override
  Invoice getDefaultTestInvoice() {
    Invoice invoice = generator
        .getTestInvoice(config.getDefaultTestInvoiceNumber(), config.getDefaultEntriesCount());
    invoice.setBuyer(getDefaultTestCompany());
    invoice.setSeller(getDefaultTestCompany());

    return invoice;
  }

  @Override
  Company getDefaultTestCompany() {
    return generator.getTestCompany(config.getDefaultTestInvoiceNumber(), "company");
  }

}
