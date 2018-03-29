package pl.coderstrust.e2e.performanceTests;

import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pl.coderstrust.e2e.model.Company;
import pl.coderstrust.e2e.model.Invoice;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class validInputTestByCustomerPerformanceTests extends AbstractValidInputPerformanceTests {

    private Company testBuyer;
    private Company testSeller;
    private long testBuyerId;
    private long testSellerId;

    private String basePathInvoice = "/invoice/";
    private String serviceVersion = "/v2/";
    private String basePathCompany = "company/";

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

    @Override
    protected String getBasePath() {
        return serviceVersion + basePathCompany + testBuyerId + "/";
    }

    @Override
    protected long addInvoice(Invoice testInvoice, long companyId) {
        String path = serviceVersion + basePathCompany + companyId + basePathInvoice;
        Response ServiceResponse = given()
                .contentType("application/json")
                .body(testInvoice)
                .when()
                .post(path);
        return getInvoiceIdFromServiceResponse(ServiceResponse.print());
    }

    @Override
    protected List<Invoice> getAllInvoicesFromDatabase() {

        List<Invoice> allInvoices = new ArrayList<>();
        String path = config.getBaseUri() + ":" + config.getBasePort();

        String response = given()
                .body(path)
                .get(serviceVersion + basePathCompany + 0 + basePathInvoice)
                .body().print();
        allInvoices.addAll(mapper.toInvoiceList(response));



        for (Company c : getAllCompaniesFromDatabase()) {
            String id = String.valueOf(c.getId());
            String response2 = given()
                    .body(path)
                    .get(serviceVersion + basePathCompany + id + basePathInvoice)
                    .body().print();
            allInvoices.addAll(mapper.toInvoiceList(response2));
        }

        return allInvoices;
    }

    @Override
    protected List<Company> getAllCompaniesFromDatabase() {
        String path = config.getBaseUri() + ":" + config.getBasePort();
        String response = given()
                .body(path)
                .get(serviceVersion + basePathCompany)
                .body().print();
        return mapper.toCompanyList(response);
    }


    @Test
    public void PrintTest() {
        System.out.println(getAllCompaniesFromDatabase().size());
    }

    @Test
    public void PrintTest2() {
        System.out.println(getAllInvoicesFromDatabase().size());
    }

    @Override
    protected long addCompany(Company testCompany) {
        Response ServiceResponse = given()
                .contentType("application/json")
                .body(testCompany)
                .when()
                .post(serviceVersion + basePathCompany);
        return getInvoiceIdFromServiceResponse(ServiceResponse.print());
    }

    @Override
    protected String getBasePathWithCompanyIdAndInvoiceId(long companyId, long invoiceId) {
        return serviceVersion + basePathCompany + companyId + basePathInvoice +
                invoiceId;
    }

    @Override
    protected String getBasePathWithDateRange(LocalDate dateFrom, LocalDate dateTo) {
        return serviceVersion + basePathCompany + testBuyerId + basePathInvoice + "&startDate=" + dateFrom + "&endDate="
                + dateTo;
    }

    
}
