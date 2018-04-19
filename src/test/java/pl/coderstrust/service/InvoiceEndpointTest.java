package pl.coderstrust.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.ws.test.server.MockWebServiceClient;
import org.springframework.xml.transform.StringSource;
import org.xml.sax.XMLFilter;
import pl.coderstrust.service.soap.InvoiceEndpoint;

import javax.xml.transform.Source;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.springframework.ws.test.server.RequestCreators.withPayload;
import static org.springframework.ws.test.server.ResponseMatchers.noFault;
import static org.springframework.ws.test.server.ResponseMatchers.payload;
import static org.springframework.ws.test.server.ResponseMatchers.validPayload;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class InvoiceEndpointTest {



    @Autowired
    private ApplicationContext applicationContext;

    private MockWebServiceClient mockClient;
    private Resource xsdSchema = new ClassPathResource("invoice.xsd");
    Source requestPayload;


    String filePathRequest = "src/test/resources/SoapXmlRequests/invoiceAddRequest.xml";
    String getInvoicesStringRequest;
    @Autowired
    private InvoiceEndpoint invoiceEndpoint;

    @Before
    public void init() throws IOException{
        mockClient = MockWebServiceClient.createClient(applicationContext);
        getInvoicesStringRequest = xmlFileRead(filePathRequest);
        requestPayload = new StringSource(getInvoicesStringRequest);
    }

    @Test
    public void shouldValidateXsdGetInvoicesResponse() throws IOException{
        String filePathResponse = "src/test/resources/SoapXmlRequests/invoiceAddResponse.xml";
        String getInvoiceStringResponse = xmlFileRead(filePathResponse);
        Source responsePayload = new StringSource(getInvoiceStringResponse);

        //when
       mockClient
                .sendRequest(withPayload(requestPayload))
                //then
                .andExpect(noFault())
                .andExpect(payload(responsePayload))
                .andExpect(validPayload(xsdSchema));
    }

    String xmlFileRead(String filePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
    }

    @Test
    public void shouldGetByIdCorrectInvoiceXml() throws IOException{
        //when
        mockClient
                .sendRequest(withPayload(requestPayload))
                //then
                .andExpect(noFault());

        String filePathRequest = "src/test/resources/SoapXmlRequests/invoiceGetByIdRequest.xml";
        String filePathResponse = "src/test/resources/SoapXmlRequests/invoiceGetByIdResponse.xml";
        String requestPyy = xmlFileRead(filePathRequest);
        String responcePyy = xmlFileRead(filePathResponse);
        Source requestPy = new StringSource(requestPyy);
        Source responcePy = new StringSource(responcePyy);
        mockClient.sendRequest(withPayload(requestPy))
                .andExpect(noFault())
                .andExpect(payload(responcePy))
                .andExpect(validPayload(xsdSchema));


    }
}
