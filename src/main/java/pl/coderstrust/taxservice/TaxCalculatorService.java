package pl.coderstrust.taxservice;

import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderstrust.database.Database;
import pl.coderstrust.model.Company;

import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.InvoiceEntry;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import pl.coderstrust.model.Product;
import pl.coderstrust.model.ProductType;
import pl.coderstrust.service.AbstractService;
import pl.coderstrust.service.CompanyService;

@Service
public class TaxCalculatorService {

  private CompanyService companyService;

  @Resource(name = "withInvoices")
  private Database<Invoice> database;

  //TODO Function which returns Company Object by company ID

  @Autowired
  public TaxCalculatorService(Database<Invoice> database, CompanyService companyService) {
    this.database = database;
    this.companyService = companyService;
  }

  public BigDecimal calculateIncome(long companyId, LocalDate beginDate, LocalDate endDate) {
    Function<Invoice, BigDecimal> getValueFunction = x -> getCompanyNameSeller(companyId).test(x)
        ? getNetValue(x) : BigDecimal.ZERO;
    return calculatePattern(getValueFunction, beginDate, endDate);
  }

  public BigDecimal calculateCost(long companyId, LocalDate beginDate, LocalDate endDate) {
    Function<Invoice, BigDecimal> getValueFunction = x -> getCompanyNameBuyer(companyId).test(x)
        ? getCostValue(x, companyService.findEntry(companyId)) : BigDecimal.ZERO;
    return calculatePattern(getValueFunction, beginDate, endDate);
  }

  public BigDecimal calculateIncomeVat(long companyId, LocalDate beginDate, LocalDate endDate) {
    Function<Invoice, BigDecimal> getValueFunction = x -> getCompanyNameBuyer(companyId).test(x)
        ? getIncomeVatValue(x, companyService.findEntry(companyId)) : BigDecimal.ZERO;
    return calculatePattern(getValueFunction, beginDate, endDate);
  }

  public BigDecimal calculateOutcomeVat(long companyId, LocalDate beginDate,
      LocalDate endDate) {
    Function<Invoice, BigDecimal> getValueFunction = x -> getCompanyNameSeller(companyId).test(x)
        ? getVatValue(x) : BigDecimal.ZERO;
    return calculatePattern(getValueFunction, beginDate, endDate);
  }

  //TODO
  public BigDecimal calculateIncomeTaxMonthAdvance() {
    return BigDecimal.valueOf(-1);
  }

  //TODO
  public BigDecimal calculateIncomeTaxQuaterAdvance() {
    return BigDecimal.valueOf(-1);
  }

  private Predicate<Invoice> getCompanyNameSeller(long companyId) {
    return x -> x.getSeller().getId() == companyId;
  }

  private Predicate<Invoice> getCompanyNameBuyer(long companyId) {
    return x -> x.getBuyer().getId() == companyId;
  }

  private Predicate<InvoiceEntry> isPersonalCarUsageAndProductTypeCar(Company company) {
    return x -> (x.getProduct().getProductType().equals(ProductType.CAR) &&
        company.isPersonalResonsCarUsage());
  }

  private BigDecimal calculatePattern(Function<Invoice, BigDecimal> getValueFunction,
      LocalDate beginDate, LocalDate endDate) {
    BigDecimal income = BigDecimal.valueOf(0);
    for (Invoice invoice : getInvoiceByDate(beginDate, endDate)) {
      income = income.add(getValueFunction.apply(invoice));
    }
    return income;
  }

  private BigDecimal getCostValue(Invoice invoice, Company company) {
    BigDecimal netValue = BigDecimal.valueOf(0);
    for (InvoiceEntry products : invoice.getProducts()) {
      Product product = products.getProduct();
      netValue = netValue
          .add(product.getNetValue()
              .multiply(new BigDecimal(products.getAmount())));
      if (isPersonalCarUsageAndProductTypeCar(company).test(products)) {
        netValue = netValue
            .add(addHalfVatValueToCost(products.getProduct(), products.getAmount()));
      }
    }
    return netValue;
  }

  private BigDecimal getNetValue(Invoice invoice) {
    BigDecimal netValue = BigDecimal.valueOf(0);
    for (InvoiceEntry products : invoice.getProducts()) {
      netValue = netValue.add(products.getProduct()
          .getNetValue().multiply(new BigDecimal(products.getAmount())));
    }
    return netValue;
  }

  private BigDecimal getVatValue(Invoice invoice) {
    BigDecimal vatValue = BigDecimal.valueOf(0);
    for (InvoiceEntry products : invoice.getProducts()) {
      vatValue = vatValue.add(products.getProduct().getNetValue()
          .multiply(BigDecimal.valueOf(products.getProduct().getVatRate().getVatPercent()))
          .multiply(new BigDecimal(products.getAmount())));
    }
    return vatValue;
  }

  private BigDecimal getIncomeVatValue(Invoice invoice, Company company) {
    BigDecimal vatValue = BigDecimal.valueOf(0);
    for (InvoiceEntry products : invoice.getProducts()) {
      if (isPersonalCarUsageAndProductTypeCar(company).test(products)) {
        vatValue = vatValue.add(addHalfVatValuetoVatValue(
            products.getProduct(), products.getAmount()));
      } else {
        vatValue = vatValue.add(products.getProduct().getNetValue()
            .multiply(BigDecimal.valueOf(products.getProduct().getVatRate().getVatPercent()))
            .multiply(new BigDecimal(products.getAmount())));
      }
    }
    return vatValue;
  }

  private List<Invoice> getInvoiceByDate(LocalDate beginDate, LocalDate endDate) {
    if (beginDate == null) {
      beginDate = LocalDate.MIN;
    }
    if (endDate == null) {
      endDate = LocalDate.MAX;
    }
    List<Invoice> selectedInvoices = new ArrayList<>();
    List<Invoice> allInvoices = database.getEntries();
    for (Invoice invoice : allInvoices) {
      if ((invoice.getIssueDate().isBefore(endDate) || invoice.getIssueDate().isEqual(endDate))
          && (invoice.getIssueDate().isAfter(beginDate) || invoice.getIssueDate()
          .isEqual(beginDate))) {
        selectedInvoices.add(invoice);
      }
    }
    return selectedInvoices;
  }

  //Method adds half of product vat to cost if needed. Polish tax system.
  private BigDecimal addHalfVatValueToCost(Product product, int amount) {
    return product.getNetValue()
        .multiply(BigDecimal.valueOf(product.getVatRate().getVatPercent()))
        .divide(BigDecimal.valueOf(2)).setScale(2, RoundingMode.CEILING)
        .multiply(BigDecimal.valueOf(amount));
  }

  private BigDecimal addHalfVatValuetoVatValue(Product product, int amount) {
    return product.getNetValue()
        .multiply(BigDecimal.valueOf(product.getVatRate().getVatPercent()))
        .divide(BigDecimal.valueOf(2)).setScale(2, RoundingMode.DOWN)
        .multiply(BigDecimal.valueOf(amount));
  }
}