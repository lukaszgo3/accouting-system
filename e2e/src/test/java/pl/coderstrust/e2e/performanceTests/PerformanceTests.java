package pl.coderstrust.e2e.performanceTests;


import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pl.coderstrust.e2e.TestsConfiguration;
import pl.coderstrust.e2e.model.Company;
import pl.coderstrust.e2e.model.Invoice;
import pl.coderstrust.e2e.testHelpers.ObjectMapperHelper;
import pl.coderstrust.e2e.testHelpers.TestCasesGenerator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.restassured.RestAssured.given;

public class PerformanceTests {


    private TestCasesGenerator generator = new TestCasesGenerator();
    private LocalDate currentDate = LocalDate.now();
    private Invoice testInvoice;
    private ArrayList<Invoice> testInvoices = new ArrayList<>();
    private TestsConfiguration config = new TestsConfiguration();
    private Pattern extractIntFromString = Pattern.compile(config.getIntFromStringRegexPattern());
    private ObjectMapperHelper mapper = new ObjectMapperHelper();
    private int THREADS_NUMBER = 5;

    private Company testBuyer;
    private Company testSeller;
    private long testBuyerId;
    private long testSellerId;

    private String basePathInvoice = "/invoice";
    private String basePathCompany = "/company";

    @BeforeClass
    public void setupClass() {
        for (int i = 0; i < config.getTestInvoicesCount(); i++) {
            testInvoices.add(generator.getTestInvoice(i + 1,
                config.getDefaultEntriesCount()));
            testInvoices.get(i).setIssueDate(currentDate.plusYears(i));
            testInvoices.get(i).setPaymentDate(currentDate.plusYears(i).plusDays(15));
        }
    }

    @BeforeMethod
    public void setupMethod() {
        currentDate = LocalDate.now();
        testInvoice = generator
            .getTestInvoice(config.getDefaultTestInvoiceNumber(), config.getDefaultEntriesCount());
        testSeller = testInvoice.getSeller();
        testBuyer = testInvoice.getBuyer();

        testSellerId = addCompany(testSeller);
        testBuyerId = addCompany(testBuyer);
        testSeller.setId(testSellerId);
        testBuyer.setId(testBuyerId);
    }


  @Test
  public void shouldCorrectlyAddAndGetInvoiceById() {

    Runnable test = new Runnable() {
      @Override
      public void run() {
    //////////
      }
    };

    final ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(THREADS_NUMBER);
    for (int i = 0; i < THREADS_NUMBER; i++) {
      newFixedThreadPool.submit(test);
    }
    newFixedThreadPool.shutdown();
    try {
      newFixedThreadPool.awaitTermination(10, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    newFixedThreadPool.shutdown();

    //    then()
    //    .assertThat()
    //    .body(jsonEquals(mapper.toJson(testInvoice)));
  }

    @Test
    public void shouldCheckDatabaseSizeAndNumberOfIds() {
        int databaseSize = getDatabaseSize();
        Set isdSet = new TreeSet();
        isdSet.addAll(getIDsFromDatabase());
        int idsCount = isdSet.size();
        org.testng.Assert.assertEquals(databaseSize, idsCount);
    }

    public int getDatabaseSize() {
        String path = config.getBaseUri() + config.getBasePort();
        String response = given()
                .body(path)
                .get("")
                .body().print();
        return mapper.toInvoiceList(response).size();
    }

    public ArrayList getIDsFromDatabase() {
        String path = config.getBaseUri() + config.getBasePort();
        String response = given()
                .body(path)
                .get("")
                .body().print();
        List<Invoice> invoicesFromDatabase = mapper.toInvoiceList(response);
        ArrayList ids = new ArrayList();
        for (Invoice invoice : invoicesFromDatabase) {
            ids.add(invoice.getId());
        }
        return ids;
    }

    public List getAllInvoicesFromDatabase() {
        String path = config.getBaseUri() + config.getBasePort();
        String response = given()
                .body(path)
                .get("")
                .body().print();
        return mapper.toInvoiceList(response);
    }

    @Test
    public void shouldUpdateInvoicesInThreadsAndCheckThem() {
        List<Invoice> invoices = getAllInvoicesFromDatabase();
        List<Invoice> updatedInvoices = new ArrayList();
        Runnable test = new Runnable() {
        AtomicInteger updatedInvoiceCount = new AtomicInteger();

            @Override
            public void run() {
                Invoice updatedInvoice = invoices.get(updatedInvoiceCount.incrementAndGet());
                updatedInvoice.setName("UPDATED INVOICE " + updatedInvoiceCount);
                given()
                        .when()
                        .contentType("application/json")
                        .body(updatedInvoice)
                        .put("/" + updatedInvoice.getId());
                updatedInvoices.add(updatedInvoice);
            }
        };

        final ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(THREADS_NUMBER);
        for (int i = 0; i < THREADS_NUMBER; i++) {
            newFixedThreadPool.submit(test);
        }
        newFixedThreadPool.shutdown();
        try {
            newFixedThreadPool.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        newFixedThreadPool.shutdown();

        for (Invoice invoice : updatedInvoices) {

            String path = config.getBaseUri() + config.getBasePort();
            String response = given()
                    .body(path)
                    .get("/" + invoice.getId())
                    .body().print();

            Assert.assertEquals(mapper.toJson(invoice), response);
        }
    }

    @Test
    public void shouldDeleteInvoicesInThreadsAndCheckDatabaseSize() {
        long startIDsCount = getIDsFromDatabase().size();
        Runnable test = new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    for (int i = 0; i < 5; i++) {
                        long invoiceId = (long) getIDsFromDatabase().get(0);
                        given()
                                .delete("" + invoiceId);

                    }
                }
            }
        };
        final ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(THREADS_NUMBER);
        for (int i = 0; i < THREADS_NUMBER; i++) {
            newFixedThreadPool.submit(test);
        }
        newFixedThreadPool.shutdown();
        try {
            newFixedThreadPool.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        newFixedThreadPool.shutdown();

        long expectedIDsCount = startIDsCount - 25;
        long actualIDsCount = getIDsFromDatabase().size();
        org.testng.Assert.assertEquals(actualIDsCount, expectedIDsCount);
    }


    @DataProvider(name = "validDates")
    Object[] validDatesProvider() {
        Object[] validDates = new Object[10];
        for (int i = 0; i < config.getTestInvoicesCount(); i++) {
            validDates[i] = LocalDate.now().plusYears(i);
        }
        return validDates;
    }

    long addCompany(Company testCompany) {
        Response ServiceResponse = given()
            .contentType("application/json")
            .body(testCompany)
            .when()
            .post(basePathCompany);
        return getInvoiceIdFromServiceResponse(ServiceResponse.print());
    }
    protected long addInvoice(Invoice testInvoice) {
      Response ServiceResponse = given()
          .contentType("application/json")
          .body(testInvoice)
          .when()
          .post(config.getBasePath());
      return getInvoiceIdFromServiceResponse(ServiceResponse.print());
  }
  protected long getInvoiceIdFromServiceResponse(String response) {
    Matcher matcher = extractIntFromString.matcher(response);
    matcher.find();
    return Long.parseLong(matcher.group(0));
  }

  protected String getBasePathWithInvoiceId(long invoiceId) {
    return basePathInvoice + "?filterKey=" + String.valueOf(testBuyerId) + "&id=" + String
        .valueOf(invoiceId);
  }


}