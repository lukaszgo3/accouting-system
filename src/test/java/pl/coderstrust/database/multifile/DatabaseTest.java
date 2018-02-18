package pl.coderstrust.database.multifile;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import pl.coderstrust.database.Database;
import pl.coderstrust.database.database.memory.ObjectMapperProvider;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.service.TestCasesGenerator;

import java.util.Random;

public abstract class DatabaseTest {

  private static final int INVOICE_ENTRIES_COUNT = 1;
  protected static final int INVOICES_COUNT = 10;

  private ObjectMapperProvider mapper = new ObjectMapperProvider();
  protected TestCasesGenerator generator = new TestCasesGenerator();
  private String[] should = new String[INVOICES_COUNT];
  private String[] output = new String[INVOICES_COUNT];
  protected Invoice testInvoice;
  protected Database database;

  public abstract Database getDatabase();

  @Before
  public void defaultGiven() throws JsonProcessingException {
    database = getDatabase();
    for (int i = 0; i < INVOICES_COUNT; i++) {
      testInvoice = generator.getTestInvoice(i, INVOICE_ENTRIES_COUNT);
      testInvoice.setSystemId(i);
      database.addInvoice(testInvoice);
      should[i] = mapper.toJson(testInvoice);
    }
  }

  @Test
  public void shouldAddAndGetSeveralInvoices() throws JsonProcessingException {
    //when
    for (int i = 0; i < INVOICES_COUNT; i++) {
      output[i] = mapper.toJson(database.getInvoiceById(i));
    }
    //then
    assertArrayEquals(should, output);
  }

  @Rule
  public ExpectedException atDeletedInvoiceAccess = ExpectedException.none();

  @Test
  public void shouldDeleteInvoicesById() {
    //when
    for (int i = 0; i < INVOICES_COUNT; i++) {
      database.deleteInvoice(i);
    }

    //then
    for (int i = 0; i < INVOICES_COUNT; i++) {
      database.getInvoiceById(i);
    }
  }

  @Test
  public void shouldUpdateInvoices() {
    //when
    try {
      for (int i = 0; i < INVOICES_COUNT; i++) {
        testInvoice = generator.getTestInvoice(i + 1, INVOICE_ENTRIES_COUNT);
        testInvoice.setSystemId(i);
        should[i] = mapper.toJson(testInvoice);
        database.updateInvoice(testInvoice);
      }

      //then
      for (int i = 0; i < INVOICES_COUNT; i++) {
        output[i] = mapper.toJson(database.getInvoiceById(i));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    assertArrayEquals(should, output);
  }

  @Test
  public void shouldReturnTrueWhenInvoiceExist() {
    Random randomInvoiceId = new Random();
    assertTrue(database.idExist(randomInvoiceId.nextInt(INVOICES_COUNT)));
  }


  @Test
  public void shouldReturnFalseWhenInvoiceDoesNotExist() {
    assertFalse(database.idExist(INVOICES_COUNT + INVOICES_COUNT));
  }

  @Test
  public void shouldReturnFalseForRemovedInvoice() {
    database.deleteInvoice(INVOICES_COUNT - 1);
    assertFalse(database.idExist(INVOICES_COUNT - 1));
  }

  @Test
  public void shouldAddAndGetSingleInvoice() throws JsonProcessingException {
    database = getDatabase();
    testInvoice = generator.getTestInvoice(1, 1);
    testInvoice.setSystemId(1);
    database.addInvoice(testInvoice);

    //when
    String output = mapper.toJson(database.getInvoiceById(1));
    String should = mapper.toJson(testInvoice);
    //then
    assertEquals(should, output);
  }

  @Rule
  public ExpectedException atNotexistantInvoiceAccess = ExpectedException.none();
}