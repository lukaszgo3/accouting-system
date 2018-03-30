package pl.coderstrust.e2e.performanceTests;

import org.testng.Assert;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.restassured.RestAssured.given;
import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static org.hamcrest.Matchers.equalTo;


public abstract class AbstractValidInputPerformanceTests {

    protected TestsConfiguration config = new TestsConfiguration();
    protected TestCasesGenerator generator = new TestCasesGenerator();
    protected ObjectMapperHelper mapper = new ObjectMapperHelper();
    protected LocalDate currentDate = LocalDate.now();
    protected Invoice testInvoice;
    protected ArrayList<Invoice> testInvoices = new ArrayList<>();
    protected Pattern extractIntFromString = Pattern.compile(config.getIntFromStringRegexPattern());
    private int THREADS_NUMBER = 5;

    protected abstract String getBasePath();

    protected abstract String getBasePathWithDateRange(LocalDate dateFrom, LocalDate dateTo);

    protected abstract String getBasePathWithCompanyIdAndInvoiceId(long companyId, long invoiceId);

    protected abstract long addInvoice(Invoice testInvoice, long companyId);

    protected abstract long addCompany(Company testcompany);

    protected abstract List<Invoice> getAllInvoicesFromDatabase();

    protected abstract List<Company> getAllCompaniesFromDatabase();

    @Test
    public void shouldReturnCorrectStatusCodeWhenServiceIsUp() {
        given()
                .when()
                .get(getBasePath())

                .then()
                .statusCode(config.getServerOkStatusCode());
    }

    protected long getInvoiceIdFromServiceResponse(String response) {
        Matcher matcher = extractIntFromString.matcher(response);
        matcher.find();
        return Long.parseLong(matcher.group(0));
    }

    @Test
    public void shouldCorrectlyUpdateInvoiceInThreads() {
        Runnable test = () -> {
            long invoiceId = addInvoice(testInvoice, 0);
            long companyId = testInvoice.getBuyer().getId();
            Invoice updatedInvoice = generator.getTestInvoice(
                    config.getDefaultTestInvoiceNumber() + 1, config.getDefaultEntriesCount());
            updatedInvoice.setId(invoiceId);
            updatedInvoice.setBuyer(testInvoice.getBuyer());
            updatedInvoice.setSeller(testInvoice.getSeller());
            given()
                    .contentType("application/json")
                    .body(updatedInvoice)
                    .when()
                    .put(getBasePathWithCompanyIdAndInvoiceId(companyId, invoiceId));

            given()
                    .when()
                    .get(getBasePathWithCompanyIdAndInvoiceId(companyId, invoiceId))
                    .then()
                    .assertThat()
                    .body(jsonEquals(mapper.toJson(updatedInvoice)));
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
    }

    @Test
    public void shouldCorrectlyDeleteInvoiceById() {
        long invoiceId = addInvoice(testInvoice, 0);
        long companyId = testInvoice.getSeller().getId();
        Runnable test = new Runnable() {
            @Override
            public void run() {
                given()
                        .contentType("application/json")
                        .body(testInvoice)
                        .when()
                        .delete(getBasePathWithCompanyIdAndInvoiceId(companyId, invoiceId));

                given()
                        .when()
                        .get(getBasePathWithCompanyIdAndInvoiceId(companyId, invoiceId))

                        .then()
                        .assertThat()
                        .body(equalTo(""));
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
    }


    @Test
    public void shouldAddSeveralInvoicesAndCheckDatabaseSize() {

        final long startDatabseSize = getAllInvoicesFromDatabase().size();
        Runnable test = new Runnable() {
            @Override
            public synchronized void run() {
                for (Invoice invoice : testInvoices) {
                    long companyId = addCompany(testInvoice.getSeller());
                    testInvoice.getSeller().setId(companyId);
                    addInvoice(testInvoice, companyId);
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

        long expectedIDsCount = startDatabseSize + 50;
        long actualIDsCount = getAllInvoicesFromDatabase().size();
        org.testng.Assert.assertEquals(actualIDsCount, expectedIDsCount);
    }


    @Test
    public void shouldAddSeveralCompaniesAndCheckCompaniesDatabaseSize() {

        final long startDatabseSize = getAllCompaniesFromDatabase().size();
        Runnable test = new Runnable() {
            @Override
            public synchronized void run() {
                for (Invoice invoice : testInvoices) {
                    long companyId = addCompany(testInvoice.getSeller());
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

        long expectedDatabaseSize = startDatabseSize + 50;
        long actualIDsCount = getAllInvoicesFromDatabase().size();
        org.testng.Assert.assertEquals(actualIDsCount, expectedDatabaseSize);
    }

    @Test(dataProvider = "validDates")
    public void shouldAddSeveralInvoicesAndFindThemByIssueDate(LocalDate newDate) {
        int invoicesAtDateCount = getInvoicesCountForDateRange(newDate, newDate);
        testInvoice.setIssueDate(newDate);
        Runnable test = new Runnable() {
            @Override
            public void run() {
                given()
                        .contentType("application/json")
                        .body(testInvoice)
                        .when()
                        .post(getBasePath());

                int invoicesAdded = getInvoicesCountForDateRange(newDate, newDate) - invoicesAtDateCount;
                Assert.assertEquals(invoicesAdded, 1);
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

    }



    @Test
    public void shouldDeleteInvoicesInThreadsAndCheckDatabaseSize() {

        long startIDsCount = getAllInvoicesFromDatabase().size();
        List<Company> allCompanies = getAllCompaniesFromDatabase();
        List<Invoice> allInvoices = getAllInvoicesFromDatabase();

        Runnable test = new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    int companyCount = 0;
                    for (int i = 0; i < 5; i++) {
                        long invoiceId = getAllInvoicesForSelectedCompany(i).get(0).getId();
                        given()
                                .delete(getBasePathWithCompanyIdAndInvoiceId(0, allInvoices.get(i).getId()));
                        companyCount++;

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
        long actualIDsCount = getAllInvoicesFromDatabase().size();
        org.testng.Assert.assertEquals(actualIDsCount, expectedIDsCount);

    }


    @DataProvider(name = "validDates")
    public Object[] validDatesProvider() {
        Object[] validDates = new Object[10];
        for (int i = 0; i < config.getTestInvoicesCount(); i++) {
            validDates[i] = LocalDate.now().plusYears(i);
        }
        return validDates;
    }

    protected int getInvoicesCountForDateRange(LocalDate dateFrom, LocalDate dateTo) {
        String path = getBasePathWithDateRange(dateFrom, dateTo);
        String response = given()
                .get(path)
                .body().print();
        return mapper.toInvoiceList(response).size();
    }

    protected List<Invoice> getAllInvoicesForSelectedCompany(long companyId) {
        ArrayList<Invoice> allInvoices = new ArrayList<>();
        String path = config.getBaseUri() + ":" + config.getBasePort();
        String response = given()
                .body(path)
                .get("/v2/company/" + companyId + "/invoice/")
                .body().print();
        mapper.toInvoiceList(response);

        return allInvoices;
    }
}