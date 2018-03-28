package pl.coderstrust.taxservice;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import pl.coderstrust.model.Company;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.Payment;
import pl.coderstrust.model.TaxType;
import pl.coderstrust.service.CompanyService;
import pl.coderstrust.testhelpers.InvoicesWithSpecifiedData;
import pl.coderstrust.testhelpers.TestCasesGenerator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TaxCalculatorControllerTest {

  private static final MediaType CONTENT_TYPE = MediaType.APPLICATION_JSON_UTF8;
  private static final String DEFAULT_PATH = "/invoice";
  private static final String MY_COMPANY_ID =
      "/1?startDate=";
  private LocalDate startDate = LocalDate.now().plusMonths(1);
  private LocalDate endDate = LocalDate.now().plusYears(1).plusMonths(1).minusDays(1);
  private LocalDate endDateInHalf = LocalDate.now().plusMonths(7).minusDays(1);


  @Autowired
  private TestCasesGenerator generator;

  @Autowired
  private ObjectMapper mapper;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  CompanyService companyService;

  @Before
  public void defaultGiven() {
    companyService.addEntry(InvoicesWithSpecifiedData.getPolishCompanySeller());
  }

  @Test
  public void calculateIncome() throws Exception {
    //given
    for (int i = 1; i <= 12; i++) {
      Invoice invoice = generator.getTestInvoice(i, 5);
      invoice.setIssueDate(LocalDate.now().plusMonths(i));
      invoice.setSeller(InvoicesWithSpecifiedData.getPolishCompanySeller());

      this.mockMvc
          .perform(post(DEFAULT_PATH)
              .content(json(invoice))
              .contentType(CONTENT_TYPE))
          .andExpect(status().isOk());
    }
    //when
    String response = this.mockMvc
        .perform(
            get("/income" + MY_COMPANY_ID + startDate + "&endDate=" + endDate))
        .andExpect(handler().methodName("calculateIncome"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", is(1170.0)))
        .andReturn()
        .getResponse()
        .getContentAsString();
    //then
    assertThat(response, is(equalTo("1170.00")));
  }

  @Test
  public void calculateCost() throws Exception {
    //given
    for (int i = 1; i <= 6; i++) {
      Invoice invoice = generator.getTestInvoice(i, 5);
      invoice.setIssueDate(LocalDate.now().plusMonths(i));
      invoice.setBuyer(InvoicesWithSpecifiedData.getPolishCompanySeller());

      this.mockMvc
          .perform(post(DEFAULT_PATH)
              .content(json(invoice))
              .contentType(CONTENT_TYPE))
          .andExpect(status().isOk());
    }
    //when
    String response = this.mockMvc
        .perform(
            get("/cost" + MY_COMPANY_ID + startDate + "&endDate=" + endDateInHalf))
        .andExpect(handler().methodName("calculateCost"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", is(315.0)))
        .andReturn()
        .getResponse()
        .getContentAsString();
    //then
    assertThat(response, is(equalTo("315.00")));
  }

  @Test
  public void calculateIncomeTax() throws Exception {
    //given
    for (int i = 1; i <= 12; i++) {
      Invoice invoice = generator.getTestInvoice(i, 5);
      invoice.setIssueDate(LocalDate.now().plusMonths(i));
      invoice.setSeller(InvoicesWithSpecifiedData.getPolishCompanySeller());

      this.mockMvc
          .perform(post(DEFAULT_PATH)
              .content(json(invoice))
              .contentType(CONTENT_TYPE))
          .andExpect(status().isOk());
    }
    for (int i = 1; i <= 6; i++) {
      Invoice invoice = generator.getTestInvoice(i, 5);
      invoice.setIssueDate(LocalDate.now().plusMonths(i));
      invoice.setBuyer(InvoicesWithSpecifiedData.getPolishCompanySeller());

      this.mockMvc
          .perform(post(DEFAULT_PATH)
              .content(json(invoice))
              .contentType(CONTENT_TYPE))
          .andExpect(status().isOk());
    }
    //when
    String response = this.mockMvc
        .perform(
            get("/incomeTax" + MY_COMPANY_ID + startDate + "&endDate=" + endDate))
        .andExpect(handler().methodName("calculateIncomeTax"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", is(855.0)))
        .andReturn()
        .getResponse()
        .getContentAsString();
    //then
    assertThat(response, is(equalTo("855.00")));
  }


  @Test
  public void calculateIncomeVat() throws Exception {
    //given
    for (int i = 1; i <= 6; i++) {
      Invoice invoice = generator.getTestInvoice(i, 5);
      invoice.setIssueDate(LocalDate.now().plusMonths(i));
      invoice.setBuyer(InvoicesWithSpecifiedData.getPolishCompanySeller());

      this.mockMvc
          .perform(post(DEFAULT_PATH)
              .content(json(invoice))
              .contentType(CONTENT_TYPE))
          .andExpect(status().isOk());
    }
    //when
    String response = this.mockMvc
        .perform(
            get("/incVat" + MY_COMPANY_ID + startDate + "&endDate=" + endDateInHalf))
        .andExpect(handler().methodName("calculateIncomeVat"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", is(72.45)))
        .andReturn()
        .getResponse()
        .getContentAsString();
    //then
    assertThat(response, is(equalTo("72.45")));
  }

  @Test
  public void calculateOutcomeVat() throws Exception {
    //given
    for (int i = 1; i <= 12; i++) {
      Invoice invoice = generator.getTestInvoice(i, 5);
      invoice.setIssueDate(LocalDate.now().plusMonths(i));
      invoice.setSeller(InvoicesWithSpecifiedData.getPolishCompanySeller());

      this.mockMvc
          .perform(post(DEFAULT_PATH)
              .content(json(invoice))
              .contentType(CONTENT_TYPE))
          .andExpect(status().isOk());
    }
    //when
    String response = this.mockMvc
        .perform(
            get("/outVat" + MY_COMPANY_ID + startDate + "&endDate=" + endDate))
        .andExpect(handler().methodName("calculateOutcomeVat"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", is(269.1)))
        .andReturn()
        .getResponse()
        .getContentAsString();
    //then
    assertThat(response, is(equalTo("269.10")));
  }

  @Test
  public void calculateDifferenceVat() throws Exception {
    //given
    for (int i = 1; i <= 12; i++) {
      Invoice invoice = generator.getTestInvoice(i, 5);
      invoice.setIssueDate(LocalDate.now().plusMonths(i));
      invoice.setSeller(InvoicesWithSpecifiedData.getPolishCompanySeller());

      this.mockMvc
          .perform(post(DEFAULT_PATH)
              .content(json(invoice))
              .contentType(CONTENT_TYPE))
          .andExpect(status().isOk());
    }
    for (int i = 1; i <= 6; i++) {
      Invoice invoice = generator.getTestInvoice(i, 5);
      invoice.setIssueDate(LocalDate.now().plusMonths(i));
      invoice.setBuyer(InvoicesWithSpecifiedData.getPolishCompanySeller());

      this.mockMvc
          .perform(post(DEFAULT_PATH)
              .content(json(invoice))
              .contentType(CONTENT_TYPE))
          .andExpect(status().isOk());
    }
    //when
    String response = this.mockMvc
        .perform(
            get("/diffVat" + MY_COMPANY_ID + startDate + "&endDate=" + endDate))
        .andExpect(handler().methodName("calculateDifferenceVat"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", is(196.65)))
        .andReturn()
        .getResponse()
        .getContentAsString();
    //then
    assertThat(response, is(equalTo("196.65")));
  }

  @Test
  public void shouldCalculateTaxSummaryProgressiveLowThresholdTaxCase() throws Exception {
    Map<String, BigDecimal> expected = new LinkedHashMap<>();
    expected.put("Income", BigDecimal.valueOf(97500));
    expected.put("Costs", BigDecimal.valueOf(11700));
    expected.put("Income - Costs", BigDecimal.valueOf(85800));
    expected.put("Pension Insurance monthly rate", Rates.getPensionInsurance());
    expected.put("Pension insurance paid", BigDecimal.valueOf(6174.84));
    expected.put("Tax calculation base", BigDecimal.valueOf(79625.16));
    expected.put("Income tax", BigDecimal.valueOf(14332.53));
    expected.put("Decreasing tax amount", Rates.getDecreasingTaxAmount());
    expected.put("Income tax - Decreasing tax amount", BigDecimal.valueOf(13776.51));
    expected.put("Income tax paid", BigDecimal.valueOf(3900));
    expected.put("Health insurance paid", BigDecimal.valueOf(3600));
    expected.put("Health insurance to substract", BigDecimal.valueOf(3100.00).setScale(2));
    expected.put("Income tax - health insurance to substract - income tax paid",
        BigDecimal.valueOf(6776.51)
    );

    taxSummaryTestPattern(TaxType.PROGRESIVE, 300, expected);
  }

  @Test
  public void shouldCalculateTaxSummaryProgressiveHighThresholdTaxCase() throws Exception {
    Map<String, BigDecimal> expected = new LinkedHashMap<>();
    expected.put("Income", BigDecimal.valueOf(195000));
    expected.put("Costs", BigDecimal.valueOf(23400));
    expected.put("Income - Costs", BigDecimal.valueOf(171600));
    expected.put("Pension Insurance monthly rate", Rates.getPensionInsurance());
    expected.put("Pension insurance paid", BigDecimal.valueOf(6174.84));
    expected.put("Tax calculation base", BigDecimal.valueOf(165425.16));
    expected.put("Income tax", BigDecimal.valueOf(40962.13));
    expected.put("Income tax paid", BigDecimal.valueOf(3900));
    expected.put("Health insurance paid", BigDecimal.valueOf(3600));
    expected.put("Health insurance to substract", BigDecimal.valueOf(3100.00).setScale(2));
    expected.put("Income tax - health insurance to substract - income tax paid",
        BigDecimal.valueOf(33962.13)
    );
    taxSummaryTestPattern(TaxType.PROGRESIVE, 600, expected);
  }

  @Test
  public void shouldReturnTaxSummaryLinearTaxCase() throws Exception {
    Map<String, BigDecimal> expected = new LinkedHashMap<>();
    expected.put("Income", BigDecimal.valueOf(97500));
    expected.put("Costs", BigDecimal.valueOf(11700));
    expected.put("Income - Costs", BigDecimal.valueOf(85800));
    expected.put("Pension Insurance monthly rate", Rates.getPensionInsurance());
    expected.put("Pension insurance paid", BigDecimal.valueOf(6174.84));
    expected.put("Tax calculation base", BigDecimal.valueOf(79625.16));
    expected.put("Income tax", BigDecimal.valueOf(15128.78));
    expected.put("Income tax paid", BigDecimal.valueOf(3900));
    expected.put("Health insurance paid", BigDecimal.valueOf(3600));
    expected.put("Health insurance to substract", BigDecimal.valueOf(3100.00).setScale(2));
    expected.put("Income tax - health insurance to substract - income tax paid",
        BigDecimal.valueOf(8128.78)
    );

    taxSummaryTestPattern(TaxType.LINEAR, 300, expected);
  }

  //TODO This method is very long but its a pattern used 3 times
  private void taxSummaryTestPattern(TaxType type, int amountMultiplier,
      Map<String, BigDecimal> expected) throws Exception {
    LocalDate startDate = LocalDate.of(LocalDate.now().plusYears(1).getYear(), 1, 1);
    LocalDate endDate = LocalDate.of(LocalDate.now().plusYears(1).getYear(), 12, 31);
    //given
    Company company = InvoicesWithSpecifiedData.getPolishCompanySeller();
    company.setId(2);
    switch (type) {
      case LINEAR: {
        company.setTaxType(TaxType.LINEAR);
        break;
      }
      case PROGRESIVE: {
        company.setTaxType(TaxType.PROGRESIVE);
        break;
      }
      default:
        throw new IllegalArgumentException("Wrong argument");
    }
    this.mockMvc
        .perform(post("/company")
            .content(json(company))
            .contentType(CONTENT_TYPE))
        .andExpect(status().isOk());

    for (int i = 1; i <= 25; i++) {
      Invoice invoice = generator.getTestInvoice(i, 1);
      invoice.setIssueDate(startDate.plusDays(i * 14));
      invoice.setSeller(company);
      invoice.getProducts().get(0).getProduct()
          .setNetValue(BigDecimal.valueOf(amountMultiplier * i));
      this.mockMvc
          .perform(post(DEFAULT_PATH)
              .content(json(invoice))
              .contentType(CONTENT_TYPE))
          .andExpect(status().isOk());
    }
    for (int i = 1; i <= 12; i++) {
      Invoice invoice = generator.getTestInvoice(i, 1);
      invoice.setIssueDate(startDate.plusDays(i * 28));
      invoice.setBuyer(company);
      invoice.getProducts().get(0).getProduct()
          .setNetValue(BigDecimal.valueOf(amountMultiplier / 2 * i));
      this.mockMvc
          .perform(post(DEFAULT_PATH)
              .content(json(invoice))
              .contentType(CONTENT_TYPE))
          .andExpect(status().isOk());
    }
    List<Payment> pensionInsurance =
        generator.createPensionInsurancePaymentsForYear(startDate.getYear());
    for (int i = 0; i < 12; i++) {
      this.mockMvc
          .perform(post("/payment/2")
              .content(json(pensionInsurance.get(i)))
              .contentType(CONTENT_TYPE))
          .andExpect(status().isOk());
    }
    List<Payment> incomeTaxAdvance =
        generator.createIncomeTaxAdvancePaymentsForYear(startDate.getYear());
    for (int i = 0; i < 12; i++) {
      this.mockMvc
          .perform(post("/payment/2")
              .content(json(incomeTaxAdvance.get(i)))
              .contentType(CONTENT_TYPE))
          .andExpect(status().isOk());
    }
    List<Payment> healthInsruace =
        generator.createHealthInsurancePaymentsForYear(startDate.getYear());
    for (int i = 0; i < 12; i++) {
      this.mockMvc
          .perform(post("/payment/2")
              .content(json(healthInsruace.get(i)))
              .contentType(CONTENT_TYPE))
          .andExpect(status().isOk());
    }
    String response = this.mockMvc
        .perform(
            get("/taxSummary/2/" + String.valueOf(startDate.getYear())))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString();
    Map<String, BigDecimal> output = getMapFromResponse(response);
    assertThat(output, is(equalTo(expected)));
  }

  private String json(Invoice invoice) throws Exception {
    return mapper.writeValueAsString(invoice);
  }

  private String json(Company company) throws Exception {
    return mapper.writeValueAsString(company);
  }

  private String json(Payment payment) throws Exception {
    return mapper.writeValueAsString(payment);
  }

  private Map<String, BigDecimal> getMapFromResponse(String response) throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    return mapper.readValue(
        response,
        new TypeReference<Map<String, BigDecimal>>() {
        });
  }
}