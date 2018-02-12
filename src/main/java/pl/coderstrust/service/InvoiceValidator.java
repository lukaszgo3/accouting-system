package pl.coderstrust.service;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class InvoiceValidator implements Validator {

  @Override
  public boolean supports(Class<?> aClass) {
    return false;
  }

  @Override
  public void validate(Object o, Errors errors) {

  }

  private boolean checkInputString(String input) {
    return (input == null || input.trim().length() == 0);
  }
}
