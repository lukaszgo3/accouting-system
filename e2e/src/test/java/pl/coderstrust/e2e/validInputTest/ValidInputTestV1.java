package pl.coderstrust.e2e.validInputTest;

import static io.restassured.RestAssured.given;

import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import pl.coderstrust.e2e.model.Invoice;

import java.time.LocalDate;

public class ValidInputTestV1 extends AbstractValidInputTests {

  private String basePath = "/v1/invoice";

  @BeforeClass
  public void setupClass() {
    currentDate = LocalDate.now();

    for (int i = 0; i < config.getTestInvoicesCount(); i++) {
      testInvoices.add(generator.getTestInvoice(i + 1,
          config.getDefaultEntriesCount()));
      testInvoices.get(i).setIssueDate(currentDate.plusYears(i));
      testInvoices.get(i).setPaymentDate(currentDate.plusYears(i).plusDays(15));
    }
  }

  @BeforeMethod
  public void setupMethod() {
    testInvoice = generator
        .getTestInvoice(config.getDefaultTestInvoiceNumber(), config.getDefaultEntriesCount());
  }

  @Override
  protected long addInvoice(Invoice testInvoice) {
    Response ServiceResponse = given()
        .contentType("application/json")
        .body(testInvoice)
        .when()
        .post(getBasePath());
    return getInvoiceIdFromServiceResponse(ServiceResponse.print());
  }

  @Override
  protected String getBasePath() {
    return basePath + "/";
  }


  @Override
  protected String getBasePathWithInvoiceId(long invoiceId) {
    return basePath + "/" + invoiceId;
  }

  @Override
  protected String getBasePathWithDateRange(LocalDate dateFrom, LocalDate dateTo) {
    return basePath + "?startDate=" + dateFrom + "&endDate=" + dateTo;
  }
}
