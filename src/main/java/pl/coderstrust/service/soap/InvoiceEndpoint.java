package pl.coderstrust.service.soap;

import model.soap.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.service.AbstractController;
import pl.coderstrust.service.InvoiceService;
import pl.coderstrust.service.filters.InvoiceDummyFilter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;


@Endpoint
public class InvoiceEndpoint extends AbstractController<Invoice> {

    private static final String NAMESPACE_URI = "http://invoice-service.com";
    private ModelToBindingClassConverter converter = new ModelToBindingClassConverter();

    @Autowired
    public InvoiceEndpoint(InvoiceService invoiceService, InvoiceDummyFilter dummyFilter) {
        super.service = invoiceService;
        super.filter = dummyFilter;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getHelloWorldRequest")
    @ResponsePayload
    public GetHelloWorldResponse getHelloWorld(@RequestPayload GetHelloWorldRequest request) {
        GetHelloWorldResponse response = new GetHelloWorldResponse();
        response.setHello("Hello my new Soap world " + request.getName());
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "invoiceAddRequest")
    @ResponsePayload
    public InvoiceAddResponse addInvoice(@RequestPayload InvoiceAddRequest request) {

        Invoice invoice = converter.soapInvoiceToInvoice(request);
        ResponseEntity resp = addEntry(invoice, null);
        InvoiceAddResponse response = new InvoiceAddResponse();
        response.setResponse(resp.toString());
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "invoiceGetByIdRequest")
    @ResponsePayload
    public InvoiceGetByIdResponse getInvoiceById(@RequestPayload InvoiceGetByIdRequest request) {

        long id = request.getId().longValue();
        InvoiceGetByIdResponse response = new InvoiceGetByIdResponse();
//        if (!service.idExist(id)) {
//            return response.setResponse();
//        }
        String invoiceStr = invoiceToXml(service.findEntry(id));
        response.setResponse(invoiceStr);
        return response;
    }

    String invoiceToXml(Invoice invoice) {
        JAXBContext jaxbContext = null;
        try {
            jaxbContext = JAXBContext.newInstance(Invoice.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            StringWriter sw = new StringWriter();
            jaxbMarshaller.marshal(invoice, sw);
            return sw.toString();
        } catch (JAXBException e) {
            //TODO: take care about this exception
            e.printStackTrace();
            return null;

        }
    }
}