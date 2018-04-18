package pl.coderstrust.service.soap;

import model.soap.GetHelloWorldRequest;
import model.soap.GetHelloWorldResponse;
import model.soap.GetInvoicePostRequest;
import model.soap.GetInvoicePostResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import pl.coderstrust.model.*;
import pl.coderstrust.service.AbstractController;
import pl.coderstrust.service.InvoiceService;
import pl.coderstrust.service.filters.InvoiceDummyFilter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.apache.naming.SelectorContext.prefix;


@Endpoint
public class InvoiceEndpoint extends AbstractController<Invoice> {

    private static final String NAMESPACE_URI = "http://invoice-service.com";

    @Autowired
    public InvoiceEndpoint(InvoiceService invoiceService, InvoiceDummyFilter dummyFilter) {
        super.service = invoiceService;
        super.filter = dummyFilter;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getHelloWorldRequest")
    @ResponsePayload
    public GetHelloWorldResponse getHelloWorld(@RequestPayload GetHelloWorldRequest request){
        GetHelloWorldResponse response = new GetHelloWorldResponse();
        response.setHello("Hello my new Soap world "+request.getName());
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getInvoicePostRequest")
    @ResponsePayload
    public GetInvoicePostResponse invoicePost(@RequestPayload GetInvoicePostRequest request){

        Invoice invoice = makeInvoice(request);
        ResponseEntity resp = addEntry(invoice,null);
        GetInvoicePostResponse response = new GetInvoicePostResponse();
        response.setResponse(resp.toString());
        return response;
    }

    private Invoice makeInvoice(GetInvoicePostRequest request){
        model.soap.Invoice invoice =request.getInvoice();
        Company seller = buildCompany(invoice.getSeller());
        Company buyer = buildCompany(invoice.getBuyer());
        InvoiceBuilder builder = new InvoiceBuilder(invoice.getId(), buyer.getName(), seller.getName());
        builder.setVisibleId(invoice.getName());
        builder.setBuyer(buyer);
        builder.setSeller(seller);
        builder.setIssueDate(LocalDate.parse(invoice.getInvoiceIssueDate()));
        builder.setPaymentDate(LocalDate.parse(invoice.getPaymentDate()));
        builder.setProducts(getEntries(invoice.getProducts()));
        builder.setPaymentState(PaymentState.valueOf(invoice.getPaymentState().value()));
        return builder.build();

    }

    private Company buildCompany(model.soap.Company company){
            CompanyBuilder builder = new CompanyBuilder(company.getName());
            builder.setIssueDate(LocalDate.parse(company.getCustomerIssueDate()));
            builder.setAddress(company.getAddress());
            builder.setCity(company.getCity());
            builder.setZipCode(company.getZipCode());
            builder.setNip(company.getNip());
            builder.setBankAccoutNumber(company.getBankAccoutNumber());
            return builder.build();
        }


    private List<InvoiceEntry> getEntries(List<model.soap.InvoiceEntry> entries) {
        int productsCount = entries.size();
        ArrayList<InvoiceEntry> invoiceEntries = new ArrayList<>(productsCount);

        for (int i = 0; i < productsCount; i++) {
            invoiceEntries.add(new InvoiceEntry(getInvoiceProduct(entries.get(i).getProduct()),entries.get(i).getAmount()));
        }
        return invoiceEntries;
    }

    private Product getInvoiceProduct(model.soap.Product product) {
        ProductBuilder builder = new ProductBuilder(product.getName(), product.getNetValue().doubleValue());
        builder.setDescription(product.getDescription());
        builder.setVatRate(Vat.valueOf(product.getVatRate().value()));
        builder.setProductType(ProductType.valueOf(product.getProductType().value()));
        return builder.build();
    }
}