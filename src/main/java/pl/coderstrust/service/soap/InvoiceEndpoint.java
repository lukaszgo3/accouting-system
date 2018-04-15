package pl.coderstrust.service.soap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

//import pl.coderstrust.model.soap.GetHelloWorldResponse;
//import pl.coderstrust.model.soap.GetHelloWorldRequest;

@Endpoint
public class InvoiceEndpoint {

    private static final String NAMESPACE_URI = "http://spring.io/guides/gs-producing-web-service";

    private InvoiceRepository invoiceRepository;

    @Autowired
    public InvoiceEndpoint(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }
//    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getHelloWorldRequest")
//    @ResponsePayload
//    public GetHelloWorldResponse getHelloWorld(@RequestPayload GetHelloWorldRequest request){
//        GetHelloWorldResponse response = new GetHelloWorldResponse();\
//        response.setHello = "Hello my new Soap world";
//        return response;
//
//    }




//    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getCountryRequest")
//    @ResponsePayload
//    public GetCountryResponse getCountry(@RequestPayload GetCountryRequest request) {
//        GetCountryResponse response = new GetCountryResponse();
//        response.setCountry(countryRepository.findCountry(request.getName()));
//
//        return response;
//    }
}
