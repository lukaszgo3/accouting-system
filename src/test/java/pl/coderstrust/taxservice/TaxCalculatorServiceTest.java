package pl.coderstrust.taxservice;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pl.coderstrust.database.Database;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.Payment;
import pl.coderstrust.model.PaymentType;
import pl.coderstrust.service.CompanyService;
import pl.coderstrust.testhelpers.InvoicesWithSpecifiedData;
import pl.coderstrust.testhelpers.TestCasesGenerator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    LocalDate startDate = LocalDate.of(LocalDate.now().getYear(), 03, 1);
    LocalDate endDate = LocalDate.of(LocalDate.now().getYear(), 3, 31);
    //given
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
  public void caluclateTaxBase() {
  }

}