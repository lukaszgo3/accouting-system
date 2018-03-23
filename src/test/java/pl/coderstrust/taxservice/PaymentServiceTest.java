package pl.coderstrust.taxservice;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pl.coderstrust.model.Company;
import pl.coderstrust.model.Payment;
import pl.coderstrust.model.PaymentType;
import pl.coderstrust.service.CompanyService;
import pl.coderstrust.testhelpers.InvoicesWithSpecifiedData;

@RunWith(MockitoJUnitRunner.class)
public class PaymentServiceTest {

  @Mock
  private CompanyService companyService;

  @InjectMocks
  private PaymentService paymentService;

  @Test
  public void test() {
    Payment payment1 = new Payment(1, LocalDate.of(2000, 1, 1), BigDecimal.TEN,
        PaymentType.HEALTH_INSURANCE);
    Company company = InvoicesWithSpecifiedData.getPolishCompanySeller();
    company.getPayments().add(payment1);

    when(companyService.findEntry(1))
        .thenReturn(company);

    System.out.println(companyService.findEntry(1).getPayments());

  }

}