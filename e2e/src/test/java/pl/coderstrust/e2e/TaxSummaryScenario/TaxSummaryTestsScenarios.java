package pl.coderstrust.e2e.TaxSummaryScenario;

import static io.restassured.RestAssured.given;
import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;

import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pl.coderstrust.e2e.TestsConfiguration;
import pl.coderstrust.e2e.model.Company;
import pl.coderstrust.e2e.model.Invoice;
import pl.coderstrust.e2e.model.Payment;
import pl.coderstrust.e2e.model.Rates;
import pl.coderstrust.e2e.model.TaxType;
import pl.coderstrust.e2e.testHelpers.ObjectMapperHelper;
import pl.coderstrust.e2e.testHelpers.TestCasesGenerator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TaxSummaryTestsScenarios {

  private ObjectMapperHelper objectMapperHelper = new ObjectMapperHelper();
  private TestCasesGenerator generator = new TestCasesGenerator();
  private LocalDate startDate;
  private TestsConfiguration testsConfiguration = new TestsConfiguration();

  @BeforeClass
  public void setup() {
    startDate = LocalDate.of(LocalDate.now().getYear() + 1, 1, 1);
  }

  @Test
  public void shouldAddCompanyPaymentsInvoicesAndReturnTaxSummaryLinearTaxCase() {
    //given
    Company company = TestUtils.GetTestCompany();
    company.setIssueDate(startDate);
    long companyId = addCompany(company);
    company.setId(companyId);
    addInvoices(company, 300);
    createAndAddPayments(companyId);

    Map<String, BigDecimal> expected = new LinkedHashMap<>();
    expected.put("Income", BigDecimal.valueOf(97500));
    expected.put("Costs", BigDecimal.valueOf(11700));
    expected.put("Income - Costs", BigDecimal.valueOf(85800));
    expected.put("Pension Insurance monthly rate", Rates.PENSION_INSURANCE.getValue());
    expected.put("Pension insurance paid", BigDecimal.valueOf(6174.84));
    expected.put("Tax calculation base", BigDecimal.valueOf(79625.16));
    expected.put("Income tax", BigDecimal.valueOf(15128.78));
    expected.put("Income tax paid", BigDecimal.valueOf(3900));
    expected.put("Health insurance paid", BigDecimal.valueOf(3600));
    expected.put("Health insurance to substract", BigDecimal.valueOf(3100.00).setScale(2));
    expected.put("Income tax - health insurance to substract - income tax paid",
        BigDecimal.valueOf(8128.78)
    );

    given()
        .when()
        .get("/taxSummary/" + String.valueOf(companyId) + "/" + String
            .valueOf(startDate.getYear()))

        .then()
        .assertThat()
        .body(jsonEquals(objectMapperHelper.toJson(expected)));
  }

  @Test
  public void shouldAddCompanyPaymentsInvoicesAndReturnTaxSummaryProgressiveTaxCaseLowThreshold() {
    //given
    Company company = TestUtils.GetTestCompany();
    company.setTaxType(TaxType.PROGRESIVE);
    company.setIssueDate(startDate);
    long companyId = addCompany(company);
    company.setId(companyId);
    addInvoices(company, 300);
    createAndAddPayments(companyId);

    Map<String, BigDecimal> expected = new LinkedHashMap<>();
    expected.put("Income", BigDecimal.valueOf(97500));
    expected.put("Costs", BigDecimal.valueOf(11700));
    expected.put("Income - Costs", BigDecimal.valueOf(85800));
    expected.put("Pension Insurance monthly rate", Rates.PENSION_INSURANCE.getValue());
    expected.put("Pension insurance paid", BigDecimal.valueOf(6174.84));
    expected.put("Tax calculation base", BigDecimal.valueOf(79625.16));
    expected.put("Income tax", BigDecimal.valueOf(14332.53));
    expected.put("Decreasing tax amount", Rates.DECREASING_TAX_AMOUNT.getValue());
    expected.put("Income tax - Decreasing tax amount", BigDecimal.valueOf(13776.51));
    expected.put("Income tax paid", BigDecimal.valueOf(3900));
    expected.put("Health insurance paid", BigDecimal.valueOf(3600));
    expected.put("Health insurance to substract", BigDecimal.valueOf(3100.00).setScale(2));
    expected.put("Income tax - health insurance to substract - income tax paid",
        BigDecimal.valueOf(6776.51)
    );

    given()
        .when()
        .get("/taxSummary/" + String.valueOf(companyId) + "/" + String
            .valueOf(startDate.getYear()))

        .then()
        .assertThat()
        .body(jsonEquals(objectMapperHelper.toJson(expected)));
  }

  @Test
  public void shouldAddCompanyPaymentsInvoicesAndReturnTaxSummaryProgressiveTaxCaseHighThreshold() {
    //given
    Company company = TestUtils.GetTestCompany();
    company.setTaxType(TaxType.PROGRESIVE);
    company.setIssueDate(startDate);
    long companyId = addCompany(company);
    company.setId(companyId);
    addInvoices(company, 600);
    createAndAddPayments(companyId);

    Map<String, BigDecimal> expected = new LinkedHashMap<>();
    expected.put("Income", BigDecimal.valueOf(195000));
    expected.put("Costs", BigDecimal.valueOf(23400));
    expected.put("Income - Costs", BigDecimal.valueOf(171600));
    expected.put("Pension Insurance monthly rate", Rates.PENSION_INSURANCE.getValue());
    expected.put("Pension insurance paid", BigDecimal.valueOf(6174.84));
    expected.put("Tax calculation base", BigDecimal.valueOf(165425.16));
    expected.put("Income tax", BigDecimal.valueOf(40962.13));
    expected.put("Income tax paid", BigDecimal.valueOf(3900));
    expected.put("Health insurance paid", BigDecimal.valueOf(3600));
    expected.put("Health insurance to substract", BigDecimal.valueOf(3100.00).setScale(2));
    expected.put("Income tax - health insurance to substract - income tax paid",
        BigDecimal.valueOf(33962.13)
    );

    given()
        .when()
        .get("/taxSummary/" + String.valueOf(companyId) + "/" + String
            .valueOf(startDate.getYear()))

        .then()
        .assertThat()
        .body(jsonEquals(objectMapperHelper.toJson(expected)));
  }

  private long addCompany(Company company) {
    Response serviceRespone =
        given()
            .contentType("application/json")
            .body(objectMapperHelper.toJson(company))
            .when()
            .post("/v2/company");
    return TestUtils.getIdFromServiceResponse(serviceRespone.print());
  }

  private void addInvoices(Company company, int amountMultiplier) {
    List<Invoice> incomeInvoices = new ArrayList<>();
    for (int i = 1; i <= 25; i++) {
      Invoice invoice = generator.getTestInvoice(i, 1);
      invoice.setIssueDate(startDate.plusDays(i * 14));
      invoice.setSeller(company);
      invoice.getProducts().get(0).getProduct()
          .setNetValue(BigDecimal.valueOf(amountMultiplier * i));
      incomeInvoices.add(invoice);

      given()
          .contentType("application/json")
          .body(objectMapperHelper.toJson(invoice))
          .when()
          .post(TestUtils.getV2InvoicePath(company.getId()));
    }
    for (int i = 1; i <= 12; i++) {
      Invoice invoice = generator.getTestInvoice(i, 1);
      invoice.setIssueDate(startDate.plusDays(i * 28));
      invoice.setBuyer(company);
      invoice.getProducts().get(0).getProduct()
          .setNetValue(BigDecimal.valueOf(amountMultiplier / 2 * i));
      given()
          .contentType("application/json")
          .body(objectMapperHelper.toJson(invoice))
          .when()
          .post(TestUtils.getV2InvoicePath(company.getId()));
    }
  }

  private void createAndAddPayments(long companyId) {
    addPayments(generator.createHealthInsurancePaymentsForYear(startDate.getYear()), companyId);
    addPayments(generator.createPensionInsurancePaymentsForYear(startDate.getYear()), companyId);
    addPayments(generator.createIncomeTaxAdvancePaymentsForYear(startDate.getYear()), companyId);
  }

  private void addPayments(List<Payment> payments, long companyId) {
    payments.forEach(payment -> {
      given()
          .contentType("application/json")
          .body(objectMapperHelper.toJson(payment))
          .when()
          .post(TestUtils.getPaymentPath(companyId));
    });
  }
}
