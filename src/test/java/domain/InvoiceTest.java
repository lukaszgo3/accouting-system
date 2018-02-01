package domain;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class InvoiceTest {

  @Test
  public void shouldAddTwoNumbers() {
    Invoice sampleInvoice = new Invoice();
    //given
    int number1 = 2;
    int number2 = 3;
    //when
    int result = sampleInvoice.add(number1, number2);
    //then
    assertThat(result, is(5));
  }
}