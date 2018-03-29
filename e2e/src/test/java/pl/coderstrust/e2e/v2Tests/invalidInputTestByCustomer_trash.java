//package pl.coderstrust.e2e;
//
//import static io.restassured.RestAssured.given;
//
//import io.restassured.response.Response;
//import org.testng.annotations.BeforeMethod;
//import pl.coderstrust.e2e.model.Company;
//import pl.coderstrust.e2e.model.Invoice;
//
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//public class invalidInputTestByCustomer extends AbstractInvalidInputTests {
//
//  private String basePath = "/invoice";
//
//  private Company testBuyer;
//  private Company testSeller;
//  private Invoice testInvoice;
//  private long testBuyerId;
//  private long testSellerId;
//
//  private String basePathInvoice = "/invoice";
//  private String basePathCompany = "/company";
//  protected Pattern extractIntFromString = Pattern.compile(config.getIntFromStringRegexPattern());
//
//  @BeforeMethod
//  public void setupMethod() {
//
//    testInvoice = generator
//        .getTestInvoice(config.getDefaultTestInvoiceNumber(), config.getDefaultEntriesCount());
//    testSeller = testInvoice.getSeller();
//    testBuyer = testInvoice.getBuyer();
//
//    testSellerId= addCompany(testSeller);
//    testBuyerId= addCompany(testBuyer);
//    testSeller.setId(testSellerId);
//    testBuyer.setId(testBuyerId);
//  }
//
//  long addCompany(Company testCompany) {
//    Response ServiceResponse = given()
//        .contentType("application/json")
//        .body(testCompany)
//        .when()
//        .post(basePathCompany);
//    return getInvoiceIdFromServiceResponse(ServiceResponse.print());
//  }
//
//  protected long getInvoiceIdFromServiceResponse(String response) {
//    Matcher matcher = extractIntFromString.matcher(response);
//    matcher.find();
//    return Long.parseLong(matcher.group(0));
//  }
//
//
//  @Override
//  protected String getBasePath(){
//    return basePath + "/";
//  }
//
//}
