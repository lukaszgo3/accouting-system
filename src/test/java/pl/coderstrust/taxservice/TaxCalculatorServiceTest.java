package pl.coderstrust.taxservice;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pl.coderstrust.database.Database;
import pl.coderstrust.model.Company;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.Payment;
import pl.coderstrust.model.PaymentType;
import pl.coderstrust.model.TaxType;
import pl.coderstrust.service.CompanyService;
import pl.coderstrust.testhelpers.InvoicesWithSpecifiedData;
import pl.coderstrust.testhelpers.TestCasesGenerator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class TaxCalculatorServiceTest {

  private static final long MY_COMPANY_ID = 1;
  private LocalDate startDate = LocalDate.now().plusMonths(1);
  private LocalDate endDate = LocalDate.now().plusYears(1).plusMonths(1).minusDays(1);
  private LocalDate endDateInHalf = LocalDate.now().plusMonths(7).minusDays(1);
  private TestCasesGenerator generator = new TestCasesGenerator();

  @Mock
  private Database database;

  @Mock
  private CompanyService companyService;

  @Mock
  private PaymentService paymentService;

  @InjectMocks
  private TaxCalculatorService taxCalculatorService;


  @Test
  public void shouldCalculateIncomeWholeInvoicesInDataRange() {
    //given
    List<Invoice> invoicesSeller = new ArrayList<>();
    for (int i = 1; i <= 12; i++) {
      Invoice invoice = generator.getTestInvoice(i, 5);
      invoice.setIssueDate(LocalDate.now().plusMonths(i));
      invoice.setSeller(InvoicesWithSpecifiedData.getPolishCompanySeller());
      invoicesSeller.add(invoice);
    }
    when(database.getEntries()).thenReturn(invoicesSeller);

    //when
    BigDecimal calculateValue = taxCalculatorService.calculateIncome(MY_COMPANY_ID, startDate,
        endDate);
    //then
    assertThat(calculateValue, is(closeTo(new BigDecimal(1170), new BigDecimal(0.001))));
  }

  @Test
  public void shouldCalculateCostWholeInvoicesInDataRange() {
    //given
    List<Invoice> invoicesBuyer = new ArrayList<>();
    for (int i = 1; i <= 6; i++) {
      Invoice invoice = generator.getTestInvoice(i, 5);
      invoice.setIssueDate(LocalDate.now().plusMonths(i));
      invoice.setBuyer(InvoicesWithSpecifiedData.getPolishCompanySeller());
      invoicesBuyer.add(invoice);
    }
    when(database.getEntries()).thenReturn(invoicesBuyer);
    //when
    BigDecimal calculateValue = taxCalculatorService.calculateCost(MY_COMPANY_ID, startDate,
        endDateInHalf);
    //then
    assertThat(calculateValue, is(closeTo(new BigDecimal(315), new BigDecimal(0.001))));
  }

  @Test
  public void shouldNotCalculateIncomeWholeInvoicesOutOfData() {
    //given
    List<Invoice> invoicesSeller = new ArrayList<>();
    for (int i = 1; i <= 12; i++) {
      Invoice invoice = generator.getTestInvoice(i, 5);
      invoice.setIssueDate(LocalDate.now().plusYears(2).plusMonths(i));
      invoice.setSeller(InvoicesWithSpecifiedData.getPolishCompanySeller());
      invoicesSeller.add(invoice);
    }
    when(database.getEntries()).thenReturn(invoicesSeller);
    //when
    BigDecimal calculateValue = taxCalculatorService.calculateIncome(MY_COMPANY_ID, startDate,
        endDate);
    //then
    assertThat(calculateValue, is(closeTo(new BigDecimal(0), new BigDecimal(0.001))));
  }

  @Test
  public void shouldNotCalculateCostWholeInvoicesOutOfData() {
    //given
    List<Invoice> invoicesBuyer = new ArrayList<>();
    for (int i = 1; i <= 12; i++) {
      Invoice invoice = generator.getTestInvoice(i, 5);
      invoice.setIssueDate(LocalDate.now().plusYears(2).plusMonths(i));
      invoice.setBuyer(InvoicesWithSpecifiedData.getPolishCompanySeller());
      invoicesBuyer.add(invoice);
    }
    when(database.getEntries()).thenReturn(invoicesBuyer);
    //when
    BigDecimal calculateValue = taxCalculatorService.calculateCost(MY_COMPANY_ID, startDate,
        endDate);
    //then
    assertThat(calculateValue, is(closeTo(new BigDecimal(0), new BigDecimal(0.001))));
  }

  @Test
  public void shouldCalculateIncomeWholeInvoicesInDataRangeAndOutOfData() {
    //given
    List<Invoice> invoicesSeller = new ArrayList<>();
    for (int i = 1; i <= 36; i++) {
      Invoice invoice = generator.getTestInvoice(i, 5);
      invoice.setIssueDate(LocalDate.now().plusMonths(i));
      invoice.setSeller(InvoicesWithSpecifiedData.getPolishCompanySeller());
      invoicesSeller.add(invoice);
    }
    when(database.getEntries()).thenReturn(invoicesSeller);
    //when
    BigDecimal calculateValue = taxCalculatorService.calculateIncome(MY_COMPANY_ID, startDate,
        endDate);
    //then
    assertThat(calculateValue, is(closeTo(new BigDecimal(1170), new BigDecimal(0.001))));
  }

  @Test
  public void shouldCalculateCostWholeInvoicesInDataRangeAndOutOfData() {
    //given
    List<Invoice> invoicesBuyer = new ArrayList<>();
    for (int i = 1; i <= 36; i++) {
      Invoice invoice = generator.getTestInvoice(i, 5);
      invoice.setIssueDate(LocalDate.now().plusMonths(i));
      invoice.setBuyer(InvoicesWithSpecifiedData.getPolishCompanySeller());
      ;
      invoicesBuyer.add(invoice);
    }
    when(companyService.findEntry(1))
        .thenReturn(InvoicesWithSpecifiedData.getPolishCompanySeller());
    when(database.getEntries()).thenReturn(invoicesBuyer);
    //when
    BigDecimal calculateValue = taxCalculatorService.calculateCost(MY_COMPANY_ID, startDate,
        endDateInHalf);
    //then
    assertThat(calculateValue, is(closeTo(new BigDecimal(315), new BigDecimal(0.001))));
  }

  @Test
  public void shouldCalculateVatOutcomeWholeInvoicesInDataRange() {
    //given
    List<Invoice> invoicesSeller = new ArrayList<>();
    for (int i = 1; i <= 12; i++) {
      Invoice invoice = generator.getTestInvoice(i, 5);
      invoice.setIssueDate(LocalDate.now().plusMonths(i));
      invoice.setSeller(InvoicesWithSpecifiedData.getPolishCompanySeller());
      invoicesSeller.add(invoice);
    }
    when(database.getEntries()).thenReturn(invoicesSeller);
    //when
    BigDecimal calculateVat = taxCalculatorService.calculateOutcomeVat(MY_COMPANY_ID, startDate,
        endDate);
    //then
    assertThat(calculateVat, is(closeTo(new BigDecimal(269.1), new BigDecimal(0.006))));
  }

  @Test
  public void shouldCalculateVatIncomeWholeInvoicesInDataRange() {
    //given
    List<Invoice> invoicesBuyer = new ArrayList<>();
    for (int i = 1; i <= 6; i++) {
      Invoice invoice = generator.getTestInvoice(i, 5);
      invoice.setIssueDate(LocalDate.now().plusMonths(i));
      invoice.setBuyer(InvoicesWithSpecifiedData.getPolishCompanySeller());
      invoicesBuyer.add(invoice);
    }
    when(database.getEntries()).thenReturn(invoicesBuyer);
    //when
    BigDecimal calculateVat = taxCalculatorService.calculateIncomeVat(MY_COMPANY_ID, startDate,
        endDate);
    //then
    assertThat(calculateVat, is(closeTo(new BigDecimal(72.45), new BigDecimal(0.006))));
  }

  @Test
  public void shouldNotCalculateVatOutcomeWholeInvoicesOutOfData() {
    //given
    List<Invoice> invoicesSeller = new ArrayList<>();
    for (int i = 1; i <= 12; i++) {
      Invoice invoice = generator.getTestInvoice(i, 5);
      invoice.setIssueDate(LocalDate.now().plusYears(2).plusMonths(i));
      invoice.setSeller(InvoicesWithSpecifiedData.getPolishCompanySeller());
      invoicesSeller.add(invoice);
    }
    when(database.getEntries()).thenReturn(invoicesSeller);
    //when
    BigDecimal calculateVat = taxCalculatorService.calculateOutcomeVat(MY_COMPANY_ID, startDate,
        endDate);
    //then
    assertThat(calculateVat, is(closeTo(new BigDecimal(0), new BigDecimal(0.006))));
  }

  @Test
  public void shouldNotCalculateVatIncomeWholeInvoicesOutOfData() {
    List<Invoice> invoicesBuyer = new ArrayList<>();
    for (int i = 1; i <= 12; i++) {
      Invoice invoice = generator.getTestInvoice(i, 5);
      invoice.setIssueDate(LocalDate.now().plusYears(2).plusMonths(i));
      invoice.setBuyer(InvoicesWithSpecifiedData.getPolishCompanySeller());
      invoicesBuyer.add(invoice);
    }
    when(database.getEntries()).thenReturn(invoicesBuyer);
    //when
    BigDecimal calculateVat = taxCalculatorService.calculateIncomeVat(MY_COMPANY_ID, startDate,
        endDate);
    //then
    assertThat(calculateVat, is(closeTo(new BigDecimal(0), new BigDecimal(0.006))));
  }

  @Test
  public void shouldCalculateVatOutcomeWholeInvoicesInDataRangeAndOutOfRange() {
    //given
    List<Invoice> invoicesSeller = new ArrayList<>();
    for (int i = 1; i <= 36; i++) {
      Invoice invoice = generator.getTestInvoice(i, 5);
      invoice.setIssueDate(LocalDate.now().plusMonths(i));
      invoice.setSeller(InvoicesWithSpecifiedData.getPolishCompanySeller());
      invoicesSeller.add(invoice);
    }
    when(database.getEntries()).thenReturn(invoicesSeller);
    //when
    BigDecimal calculateVat = taxCalculatorService.calculateOutcomeVat(MY_COMPANY_ID, startDate,
        endDate);
    //then
    assertThat(calculateVat, is(closeTo(new BigDecimal(269.1), new BigDecimal(0.006))));
  }

  @Test
  public void shouldCalculateVatIncomeWholeInvoicesInDataRangeAndOutOfRange() {
    List<Invoice> invoicesBuyer = new ArrayList<>();
    for (int i = 1; i <= 36; i++) {
      Invoice invoice = generator.getTestInvoice(i, 5);
      invoice.setIssueDate(LocalDate.now().plusMonths(i));
      invoice.setBuyer(InvoicesWithSpecifiedData.getPolishCompanySeller());
      invoicesBuyer.add(invoice);
    }
    when(companyService.findEntry(1))
        .thenReturn(InvoicesWithSpecifiedData.getPolishCompanySeller());
    when(database.getEntries()).thenReturn(invoicesBuyer);
    //when
    BigDecimal calculateVat = taxCalculatorService.calculateIncomeVat(MY_COMPANY_ID, startDate,
        endDateInHalf);
    //then
    assertThat(calculateVat, is(closeTo(new BigDecimal(72.45), new BigDecimal(0.006))));
  }


  @Test
  public void shouldCalculateIncomeTaxAdvanceLinearTax() {
    //given
    final LocalDate startDate = LocalDate.of(LocalDate.now().getYear(), 03, 1);
    final LocalDate endDate = LocalDate.of(LocalDate.now().getYear(), 3, 31);

    List<Invoice> invoices = new ArrayList<>();
    for (int i = 1; i <= 25; i++) {
      Invoice invoice = generator.getTestInvoice(i, 1);
      invoice.setIssueDate(startDate.plusDays(i));
      invoice.setSeller(InvoicesWithSpecifiedData.getPolishCompanySeller());
      invoice.getProducts().get(0).getProduct().setNetValue(BigDecimal.valueOf(100 * i));
      invoices.add(invoice);
    }
    for (int i = 1; i <= 5; i++) {
      Invoice invoice = generator.getTestInvoice(i, 1);
      invoice.setIssueDate(startDate.plusDays(i));
      invoice.setBuyer(InvoicesWithSpecifiedData.getPolishCompanySeller());
      invoice.getProducts().get(0).getProduct().setNetValue(BigDecimal.valueOf(50 * i));
      invoices.add(invoice);
    }

    Payment pensionInsurance = new Payment(1, startDate,
        BigDecimal.valueOf(500), PaymentType.PENSION_INSURANCE);
    Payment healthInsurance = new Payment(2, startDate,
        BigDecimal.valueOf(300), PaymentType.HEALTH_INSURANCE);
    Payment incomeTaxAdvance = new Payment(3, startDate,
        BigDecimal.valueOf(1100), PaymentType.INCOME_TAX_ADVANCE);

    when(database.getEntries()).thenReturn(invoices);
    when(companyService.findEntry(1))
        .thenReturn(InvoicesWithSpecifiedData.getPolishCompanySeller());
    when(paymentService.getPaymentsByTypeAndDate(1,
        LocalDate.of(startDate.getYear(), 1, 1),
        endDate.plusDays(20), PaymentType.PENSION_INSURANCE))
        .thenReturn(Arrays.asList(pensionInsurance));
    when(paymentService.getPaymentsByTypeAndDate(1,
        LocalDate.of(startDate.getYear(), 1, 1),
        endDate, PaymentType.INCOME_TAX_ADVANCE))
        .thenReturn(Arrays.asList(incomeTaxAdvance));
    when(paymentService.getPaymentsByTypeAndDate(1,
        LocalDate.of(startDate.getYear(), 1, 1),
        endDate.plusDays(20), PaymentType.HEALTH_INSURANCE))
        .thenReturn(Arrays.asList(healthInsurance));

    //when
    BigDecimal taxAdvance = taxCalculatorService.calculateIncomeTaxAdvance(
        1, startDate, endDate);
    //then
    assertThat(taxAdvance, is(BigDecimal.valueOf(4579.17)));
  }

  @Test
  public void shouldCalculateProgressiveTaxAdvanceLinearTaxType() {
    //when
    incomeTaxAdvanceCalculatorTestPattern(
        TaxType.LINEAR, 100, BigDecimal.valueOf(4579.17));

  }

  @Test
  public void shouldCalculateProgressiveTaxAdvanceLowTreshold() {
    //when
    incomeTaxAdvanceCalculatorTestPattern(
        TaxType.PROGRESIVE, 100, BigDecimal.valueOf(3710.65));

  }

  @Test
  public void shouldCalculateProgressiveTaxAdvanceHighTreshold() {
    incomeTaxAdvanceCalculatorTestPattern(
        TaxType.PROGRESIVE, 1000, BigDecimal.valueOf(88107.75));
  }

  private void incomeTaxAdvanceCalculatorTestPattern(TaxType type, int amountMultiplier,
      BigDecimal expectedValue) {
    //given
    final LocalDate startDate = LocalDate.of(LocalDate.now().getYear(), 03, 1);
    final LocalDate endDate = LocalDate.of(LocalDate.now().getYear(), 3, 31);
    Company company = InvoicesWithSpecifiedData.getPolishCompanySeller();
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
    List<Invoice> invoices = new ArrayList<>();
    for (int i = 1; i <= 25; i++) {
      Invoice invoice = generator.getTestInvoice(i, 1);
      invoice.setIssueDate(startDate.plusDays(i));
      invoice.setSeller(company);
      invoice.getProducts().get(0).getProduct()
          .setNetValue(BigDecimal.valueOf(amountMultiplier * i));
      invoices.add(invoice);
    }
    for (int i = 1; i <= 5; i++) {
      Invoice invoice = generator.getTestInvoice(i, 1);
      invoice.setIssueDate(startDate.plusDays(i));
      invoice.setBuyer(company);
      invoice.getProducts().get(0).getProduct()
          .setNetValue(BigDecimal.valueOf(amountMultiplier / 2 * i));
      invoices.add(invoice);
    }

    Payment pensionInsurance = new Payment(1, startDate,
        BigDecimal.valueOf(500), PaymentType.PENSION_INSURANCE);
    Payment healthInsurance = new Payment(2, startDate,
        BigDecimal.valueOf(300), PaymentType.HEALTH_INSURANCE);
    Payment incomeTaxAdvance = new Payment(3, startDate,
        BigDecimal.valueOf(1100), PaymentType.INCOME_TAX_ADVANCE);

    when(database.getEntries()).thenReturn(invoices);
    when(companyService.findEntry(1))
        .thenReturn(company);
    when(paymentService.getPaymentsByTypeAndDate(1,
        LocalDate.of(startDate.getYear(), 1, 1),
        endDate.plusDays(20), PaymentType.PENSION_INSURANCE))
        .thenReturn(Arrays.asList(pensionInsurance));
    when(paymentService.getPaymentsByTypeAndDate(1,
        LocalDate.of(startDate.getYear(), 1, 1),
        endDate, PaymentType.INCOME_TAX_ADVANCE))
        .thenReturn(Arrays.asList(incomeTaxAdvance));
    when(paymentService.getPaymentsByTypeAndDate(1,
        LocalDate.of(startDate.getYear(), 1, 1),
        endDate.plusDays(20), PaymentType.HEALTH_INSURANCE))
        .thenReturn(Arrays.asList(healthInsurance));

    //when
    BigDecimal output = taxCalculatorService.calculateIncomeTaxAdvance(
        1, startDate, endDate);

    //then
    assertThat(output, is(expectedValue));
  }

  @Test
  public void shouldCalculateTaxSummaryLinearTaxCase() {
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

    taxSummaryTestPattern(TaxType.LINEAR, 300, expected);
  }

  @Test
  public void shouldCalculateTaxSummaryProgressiveLowThresholdTaxCase() {
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

    taxSummaryTestPattern(TaxType.PROGRESIVE, 300, expected);
  }

  @Test
  public void shouldCalculateTaxSummaryProgressiveHighThresholdTaxCase() {
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
    taxSummaryTestPattern(TaxType.PROGRESIVE, 600, expected);
  }

  private void taxSummaryTestPattern(TaxType type, int amountMultiplier,
      Map<String, BigDecimal> expected) {
    final LocalDate startDate = LocalDate.of(LocalDate.now().getYear(), 1, 1);
    final LocalDate endDate = LocalDate.of(LocalDate.now().getYear(), 12, 31);
    //given
    Company company = InvoicesWithSpecifiedData.getPolishCompanySeller();
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
    List<Invoice> invoices = new ArrayList<>();
    for (int i = 1; i <= 25; i++) {
      Invoice invoice = generator.getTestInvoice(i, 1);
      invoice.setIssueDate(startDate.plusDays(i * 14));
      invoice.setSeller(company);
      invoice.getProducts().get(0).getProduct()
          .setNetValue(BigDecimal.valueOf(amountMultiplier * i));
      invoices.add(invoice);
    }
    for (int i = 1; i <= 12; i++) {
      Invoice invoice = generator.getTestInvoice(i, 1);
      invoice.setIssueDate(startDate.plusDays(i * 28));
      invoice.setBuyer(company);
      invoice.getProducts().get(0).getProduct()
          .setNetValue(BigDecimal.valueOf(amountMultiplier / 2 * i));
      invoices.add(invoice);
    }
    when(database.getEntries()).thenReturn(invoices);
    when(companyService.findEntry(1))
        .thenReturn(company);
    when(paymentService.getPaymentsByTypeAndDate(1,
        startDate,
        endDate.plusDays(20), PaymentType.PENSION_INSURANCE))
        .thenReturn(generator.createPensionInsurancePaymentsForYear(LocalDate.now().getYear()));
    when(paymentService.getPaymentsByTypeAndDate(1,
        startDate,
        endDate.plusDays(20), PaymentType.INCOME_TAX_ADVANCE))
        .thenReturn(generator.createIncomeTaxAdvancePaymentsForYear(LocalDate.now().getYear()));
    when(paymentService.getPaymentsByTypeAndDate(1,
        startDate,
        endDate.plusDays(20), PaymentType.HEALTH_INSURANCE))
        .thenReturn(generator.createHealthInsurancePaymentsForYear(LocalDate.now().getYear()));
    //when
    Map<String, BigDecimal> output = taxCalculatorService
        .taxSummary(1, LocalDate.now().getYear());
    //then
    assertThat(output, is(equalTo(expected)));
  }
}


