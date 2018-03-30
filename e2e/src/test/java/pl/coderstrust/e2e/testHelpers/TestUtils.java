package pl.coderstrust.e2e.testHelpers;

import static io.restassured.RestAssured.given;

import io.restassured.response.Response;
import pl.coderstrust.e2e.TestsConfiguration;
import pl.coderstrust.e2e.model.Company;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestUtils {

  private static Pattern extractIntFromString = Pattern
      .compile(new TestsConfiguration().getIntFromStringRegexPattern());

  public static long getInvoiceIdFromServiceResponse(String response) {
    Matcher matcher = extractIntFromString.matcher(response);
    matcher.find();
    return Long.parseLong(matcher.group(0));
  }

  public static String getV1InvoicePath() {
    return "/v1/invoice/";
  }

  public static String getV1InvoicePathWithInvoiceId(long invoiceId) {
    return getV1InvoicePath() + String.valueOf(invoiceId);
  }

  public static String getV2InvoicePath(long companyId) {
    return "v2/company/" + String.valueOf(companyId) + "/invoice/";
  }

  public static String getV2InvoicePathWithInvoiceId(long companyId, long invoiceId) {
    return getV2InvoicePath(companyId) + String.valueOf(invoiceId);
  }

  public static String getV2CompanyPath() {
    return "v2/company";
  }

  public static long registerCompany(Company testCompany) {
    Response ServiceResponse = given()
        .contentType("application/json")
        .body(testCompany)
        .when()
        .post(getV2CompanyPath());
    return getInvoiceIdFromServiceResponse(ServiceResponse.print());
  }

}
