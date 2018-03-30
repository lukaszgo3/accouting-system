package pl.coderstrust.e2e.invalidInputTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import io.restassured.response.Response;
import org.testng.annotations.Test;
import pl.coderstrust.e2e.model.Company;
import pl.coderstrust.e2e.model.Invoice;
import pl.coderstrust.e2e.model.Messages;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InvalidInputTestV2 extends AbstractInvalidInputTests {

  private String basePathInvoice = "v2/company/";
  private String basePathCompany = "v2/company";
  private Company testBuyer;
  private Company testSeller;
  private long testBuyerId;
  private long testSellerId;
  private long otherCompanyId;


  protected Pattern extractIntFromString = Pattern.compile(config.getIntFromStringRegexPattern());

  @Override
  protected String getBasePath() {
    return basePathInvoice + String.valueOf(testBuyerId) + "/invoice/";
  }

  private String getBasePath(long companyId) {
    return basePathInvoice + String.valueOf(companyId) + "/invoice/";
  }

  protected String getBasePathWithInvoiceId(long invoiceId) {
    return basePathInvoice + String.valueOf(testBuyerId) + "/invoice/" + String
        .valueOf(invoiceId);
  }

  protected String getBasePathWithInvoiceId(long companyId, long invoiceId) {
    return basePathInvoice + String.valueOf(companyId) + "/invoice/" + String
        .valueOf(invoiceId);
  }

  @Override
  Invoice getDefaultTestInvoice() {
    Invoice testInvoice = generator
        .getTestInvoice(config.getDefaultTestInvoiceNumber(), config.getDefaultEntriesCount());
    testSeller = testInvoice.getSeller();
    testBuyer = testInvoice.getBuyer();

    testSellerId = addCompany(testSeller);
    testBuyerId = addCompany(testBuyer);
    testSeller.setId(testSellerId);
    testBuyer.setId(testBuyerId);

    otherCompanyId = addCompany(testSeller);

    return testInvoice;
  }

  long addCompany(Company testCompany) {
    Response ServiceResponse = given()
        .contentType("application/json")
        .body(testCompany)
        .when()
        .post(basePathCompany);
    return getInvoiceIdFromServiceResponse(ServiceResponse.print());
  }

  protected long getInvoiceIdFromServiceResponse(String response) {
    Matcher matcher = extractIntFromString.matcher(response);
    matcher.find();
    return Long.parseLong(matcher.group(0));
  }

  @Override
  Company getDefaultTestCompany() {
    return generator.getTestCompany(config.getDefaultTestInvoiceNumber(), "company");
  }

  @Test
  public void shouldReturnErrorWhileInvoiceAddWhenCompanyIdNotExist() {
    Invoice testInvoice = getDefaultTestInvoice();
    given()
        .contentType("application/json")
        .body(testInvoice)
        .when()
        .post(getBasePath(otherCompanyId + 1))
        .then()
        .assertThat()
        .statusCode(config.getServerEntryNotExistStatusCode());
  }

  @Test
  public void shouldNotAddInvoiceWhenCompanyNotMatch() {
    Invoice testInvoice = getDefaultTestInvoice();
    given()
        .contentType("application/json")
        .body(testInvoice)
        .when()
        .post(getBasePath(otherCompanyId))
        .then()
        .assertThat()
        .body(containsString(Messages.COMPANY_ID_NOT_MATCH));
  }

  @Test
  public void shouldNotGetInvoiceWhenCompanyIdNotMatch() {
    Invoice testInvoice = getDefaultTestInvoice();
    given()
        .contentType("application/json")
        .body(testInvoice)
        .when()
        .get(getBasePathWithInvoiceId(otherCompanyId, testInvoice.getId()))
        .then()
        .assertThat()
        .body(containsString(""));
  }

  @Test
  public void shouldNotUpdateInvoiceWhenCompanyIdNotMatch() {
    Invoice testInvoice = getDefaultTestInvoice();
    given()
        .contentType("application/json")
        .body(testInvoice)
        .when()
        .put(getBasePathWithInvoiceId(otherCompanyId, testInvoice.getId()))
        .then()
        .assertThat()
        .body(containsString(Messages.COMPANY_ID_NOT_MATCH));
  }

  @Test
  public void shouldNotDeleteInvoiceWhenCompanyIdNotMatch() {
    Invoice testInvoice = getDefaultTestInvoice();
    given()
        .contentType("application/json")
        .body(testInvoice)
        .when()
        .delete(getBasePathWithInvoiceId(otherCompanyId, testInvoice.getId()))
        .then()
        .assertThat()
        .body(containsString(Messages.COMPANY_ID_NOT_MATCH));
  }

}
