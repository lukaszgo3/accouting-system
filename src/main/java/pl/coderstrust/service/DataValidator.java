package pl.coderstrust.service;

import org.springframework.stereotype.Service;
import pl.coderstrust.model.Company;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.Product;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class DataValidator {

  public List<String> validateInvoice(Invoice invoice) {

    List<String> errors = new ArrayList<>();

    if (checkInputString(invoice.getVisibleId())) {
      errors.add("Invoice id is empty");
    }

    errors.addAll(companyChecker(invoice.getSeller()));
    errors.addAll(companyChecker(invoice.getBuyer()));
    errors.addAll(dateChecker(invoice.getIssueDate()));
    errors.addAll(dateChecker(invoice.getPaymentDate()));

    if (invoice.getProducts().size() == 0) {
      errors.add("Product list is empty,add at least one ");
    } else {
      for (int i = 0; i < invoice.getProducts().size(); i++) {
        errors.addAll(productChecker(invoice.getProducts().get(i).getProduct()));
      }
    }

    if (invoice.getPaymentState() == null) {
      errors.add("Payment state is empty");
    }

    if (errors.isEmpty()) {
      errors.add("Invoice is ok");
    }
    return errors;
  }

  public List<String> companyChecker(Company company) {

    List<String> errors = new ArrayList<>();
    if (checkInputString(company.getAddress())) {
      errors.add("Company addres is empty");
    }
    if (checkInputString(company.getCity())) {
      errors.add("Company's city is empty");
    }
    if (checkInputString(company.getNip())) {
      errors.add("Company's nip is empty");
    }
    if (checkInputString(company.getZipCode())) {
      errors.add("Company's zip code is empty");
    }
    if (checkInputString(company.getBankAccoutNumber())) {
      errors.add("Company's bank account number code is empty");
    }

    return errors;
  }

  public List<String> dateChecker(LocalDate date) {
    if (date == null) {
      return Arrays.asList("Date is empty");
    }
    List<String> errors = new ArrayList<>();
    if (date.isBefore(LocalDate.now())) {
      errors.add("Date can't be earlier then actual date");
    }
    return errors;
  }

  public List<String> productChecker(Product product) {

    List<String> errors = new ArrayList<>();

    if (checkInputString(product.getName())) {
      errors.add("Product's name is empty");
    }

    if (checkInputString(product.getDescription())) {
      errors.add("Product's description is empty");
    }

    if (product.getVatRate() == null) {
      errors.add("Product's vat rate is empty");
    }

    if (product.getNetValue() == null) {
      errors.add("Product's value is empty");
    }

    if (product.getNetValue().compareTo(BigDecimal.ZERO) <= 0) {
      errors.add("Product net value is lower or equal to 0 ");
    }
    return errors;
  }

  private boolean checkInputString(String input) {
    return ((input == null || input.trim().length() == 0));
  }

}
