package pl.coderstrust.e2e.performanceTests;

import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import pl.coderstrust.e2e.model.Company;
import pl.coderstrust.e2e.model.Invoice;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class validInputTestPerformanceTests extends AbstractValidInputPerformanceTests {

  private String basePath = "invoice";
  private String serviceVersion = "/v1/";

  @BeforeClass
  public void setupClass() {
    for (int i = 0; i < config.getTestInvoicesCount(); i++) {
      testInvoices.add(generator.getTestInvoice(i + 1,
              config.getDefaultEntriesCount()));
      testInvoices.get(i).setIssueDate(currentDate.plusYears(i));
      testInvoices.get(i).setPaymentDate(currentDate.plusYears(i).plusDays(15));
    }
  }

  @BeforeMethod
  public void setupMethod() {
    currentDate = LocalDate.now();
    testInvoice = generator
            .getTestInvoice(config.getDefaultTestInvoiceNumber(), config.getDefaultEntriesCount());
  }

  @Override
  protected long addInvoice(Invoice testInvoice, long companyId) {
    Response ServiceResponse = given()
            .contentType("application/json")
            .body(testInvoice)
            .when()
            .post(getBasePath());
    return getInvoiceIdFromServiceResponse(ServiceResponse.print());
  }

  @Override
  protected long addCompany(Company testcompany) {
    return 0;
  }

  @Override
  protected List<Invoice> getAllInvoicesFromDatabase() {
    String path = config.getBaseUri() + ":" +config.getBasePort();
    String response = given()
            .body(path)
            .get(serviceVersion+basePath)
            .body().print();
    return mapper.toInvoiceList(response);
  }

  @Override
  protected List<Company> getAllCompaniesFromDatabase() {
    List<Company> emptyList = new ArrayList<>(0);
    return emptyList;
  }

  @Override
  protected String getBasePath() {
    return serviceVersion+basePath + "/";
  }

  @Override
  protected String getBasePathWithCompanyIdAndInvoiceId(long companyId, long invoiceId) {
    return serviceVersion+basePath + "/" +invoiceId;
  }


  @Override
  protected String getBasePathWithDateRange(LocalDate dateFrom, LocalDate dateTo) {
    return serviceVersion+basePath + "?startDate=" + dateFrom + "&endDate=" + dateTo;
  }
}

