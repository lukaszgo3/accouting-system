package pl.coderstrust.e2e.invalidInputTest;

import pl.coderstrust.e2e.model.Invoice;
import pl.coderstrust.e2e.testHelpers.TestUtils;

public class InvalidInputTestsV1 extends AbstractInvalidInputTests {

  @Override
  protected String getBasePath() {
    return TestUtils.getV1InvoicePath();
  }

  @Override
  Invoice getDefaultTestInvoice() {
    return generator
        .getTestInvoice(config.getDefaultTestInvoiceNumber(), config.getDefaultEntriesCount());
  }
}
