package pl.coderstrust.e2e.validInputTest;

import static io.restassured.RestAssured.given;

import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import pl.coderstrust.e2e.model.Company;
import pl.coderstrust.e2e.model.Invoice;

import java.time.LocalDate;

public class ValidInputTestV2 extends AbstractValidInputTests {

  private Company testBuyer;
  private Company testSeller;
  private long testBuyerId;
  private long testSellerId;

  private String basePathInvoice = "v2/company/";
  private String basePathCompany = "v2/company";

  @BeforeClass
  public void setupClass() {
    for (int i = 0; i < config.getTestInvoicesCount(); i++) {
      testInvoices.add(generator.getTestInvoice(i + 1,
          config.getDefaultEntriesCount()));
      testInvoices.get(i).setIssueDate(currentDate.plusYears(i));
      testInvoices.get(i).setPaymentDate(currentDate.plusYears(i).plusDays(15));
      testInvoices.get(i).getSeller().setId(addCompany(testInvoices.get(i).getSeller()));

    }
  }

  @BeforeMethod
  public void setupMethod() {
    currentDate = LocalDate.now();
    testInvoice = generator
        .getTestInvoice(config.getDefaultTestInvoiceNumber(), config.getDefaultEntriesCount());
    testSeller = testInvoice.getSeller();
    testBuyer = testInvoice.getBuyer();

    testSellerId = addCompany(testSeller);
    testBuyerId = addCompany(testBuyer);
    testSeller.setId(testSellerId);
    testBuyer.setId(testBuyerId);







  }

  @Override
  protected String getBasePath() {
    return basePathInvoice + String.valueOf(testBuyerId) + "/invoice/";
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

  long addCompany(Company testCompany) {
    Response ServiceResponse = given()
        .contentType("application/json")
        .body(testCompany)
        .when()
        .post(basePathCompany);
    return getInvoiceIdFromServiceResponse(ServiceResponse.print());
  }

  @Override
  protected String getBasePathWithInvoiceId(long invoiceId) {
    return basePathInvoice + String.valueOf(testBuyerId) + "/invoice/" + String
        .valueOf(invoiceId);
  }

  @Override
  protected String getBasePathWithDateRange(LocalDate dateFrom, LocalDate dateTo) {
    return basePathInvoice + testBuyerId + "/invoice/?startDate=" + dateFrom + "&endDate="
        + dateTo;
  }
}