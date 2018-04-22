package pl.coderstrust.service;


import static org.junit.Assert.assertEquals;
import static org.springframework.ws.test.server.RequestCreators.withPayload;
import static org.springframework.ws.test.server.ResponseMatchers.noFault;
import static org.springframework.ws.test.server.ResponseMatchers.payload;
import static org.springframework.ws.test.server.ResponseMatchers.validPayload;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.test.server.MockWebServiceClient;
import org.springframework.ws.test.server.ResponseMatcher;
import org.springframework.xml.transform.StringSource;
import org.w3c.dom.Document;
import pl.coderstrust.service.soap.bindingClasses.InvoiceGetByDateResponse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class InvoiceEndpointTest {

  private final String resourcesPath = "src/test/resources/SoapXmlRequests/";

  @Autowired
  private ApplicationContext applicationContext;
  private MockWebServiceClient mockClient;
  private Resource xsdSchema = new ClassPathResource("invoice.xsd");

  @Before
  public void init() throws IOException {
    mockClient = MockWebServiceClient.createClient(applicationContext);
  }

  @Test
  public void shouldAddInvoice() throws IOException {
    Source requestPayload = getRequest("invoiceAddRequest.xml");
    //when
    mockClient
        .sendRequest(withPayload(requestPayload))
        .andExpect(noFault())
        .andExpect(validPayload(xsdSchema))
        .andExpect(new ContainsString("No errors."));
  }

  @Test
  public void shouldGeInvoiceById() throws IOException {

    Source requestPayload = getRequest("invoiceAddRequest.xml");
    //when
    mockClient
        .sendRequest(withPayload(requestPayload))
        .andExpect(noFault());

    requestPayload = getRequest("invoiceGetByIdRequest.xml");
    Source responsePayload = getRequest("invoiceGetByIdResponse.xml");

    //when
    mockClient.sendRequest(withPayload(requestPayload))
        //then
        .andExpect(noFault())
        .andExpect(new ContainsString("No errors."))
        .andExpect(validPayload(xsdSchema));
  }

  @Test
  public void shouldGetInvoicesByDate() throws Exception {
    Source requestPayload = getRequest("invoiceAddRequestDateChanged.xml");

    for (int i = 0; i < 10; i++) {
      //when
      mockClient
          .sendRequest(withPayload(requestPayload))
          //then
          .andExpect(noFault());
    }

    requestPayload = getRequest("invoiceGetByDateRequest.xml");
    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    //when
    mockClient.sendRequest(withPayload(requestPayload))
        //then
        .andExpect(noFault())
        .andExpect(validPayload(xsdSchema))
        .andExpect(
            new ResponseMatcher() {
              @Override
              public void match(WebServiceMessage request, WebServiceMessage response)
                  throws IOException, AssertionError {
                response.writeTo(stream);
              }
            });
    assertEquals(countInvoices(stream.toString()), 10);
  }

  @Test
  public void shouldUpdateInvoice() throws Exception {
    Source requestPayload = getRequest("invoiceAddRequest.xml");

    mockClient
        .sendRequest(withPayload(requestPayload))
        //then
        .andExpect(noFault());

    requestPayload = getRequest("invoiceUpdateRequest.xml");

    mockClient
        .sendRequest(withPayload(requestPayload))
        //then
        .andExpect(noFault());

    requestPayload = getRequest("invoiceGetByIdRequest.xml");
    Source responsePayload = getRequest("invoiceUpdateResponse.xml");
    mockClient
        .sendRequest(withPayload(requestPayload))
        .andExpect(noFault())
        .andExpect(validPayload(xsdSchema))
        .andExpect(payload(responsePayload));
  }

  @Test
  public void shouldRemoveInvoice() throws Exception {
    Source requestPayload = getRequest("invoiceAddRequest.xml");

    for (int i = 0; i < 2; i++) {
      mockClient
          .sendRequest(withPayload(requestPayload))
          //then
          .andExpect(noFault());
    }
    requestPayload = getRequest("invoiceRemoveRequest.xml");
    mockClient
        .sendRequest(withPayload(requestPayload))
        .andExpect(noFault())
        .andExpect(new ContainsString("No errors."));

    requestPayload = getRequest("invoiceGetByRemovedIdRequest.xml");
    mockClient
        .sendRequest(withPayload(requestPayload))
        .andExpect(noFault())
        .andExpect(new ContainsString("Invoice with specified id does not exist."));
  }

  private int countInvoices(String soapBodyXml) throws Exception {
    SOAPMessage msg = MessageFactory.newInstance().createMessage(null,
        new ByteArrayInputStream(soapBodyXml.getBytes(StandardCharsets.UTF_8)));
    InvoiceGetByDateResponse response = unmarshal(convertToString(msg.getSOAPBody()));
    return response.getInvoiceList().getInvoice().size();
  }

  public InvoiceGetByDateResponse unmarshal(String input) throws Exception {
    JAXBContext context = JAXBContext.newInstance(InvoiceGetByDateResponse.class);
    Unmarshaller um = context.createUnmarshaller();
    return (InvoiceGetByDateResponse) um.unmarshal(new StringReader(input));
  }

  private Source getRequest(String fileName) throws IOException {
    String xmlRequest = xmlFileRead(this.resourcesPath + fileName);
    return new StringSource(xmlRequest);
  }

  private String xmlFileRead(String filePath) throws IOException {
    return new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
  }

  private String convertToString(SOAPBody message) throws Exception {

    TransformerFactory tf = TransformerFactory.newInstance();
    Transformer transformer = null;
    transformer = tf.newTransformer();
    transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
    transformer.setOutputProperty(OutputKeys.METHOD, "xml");
    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

    Document doc = message.extractContentAsDocument();
    StringWriter sw = new StringWriter();
    transformer.transform(new DOMSource(doc), new StreamResult(sw));
    return sw.toString();
  }

  class ContainsString implements ResponseMatcher {

    String phrase;

    public ContainsString(String phrase) {
      this.phrase = phrase;
    }

    @Override
    public void match(WebServiceMessage webServiceMessage, WebServiceMessage webServiceMessage1)
        throws IOException, AssertionError {

      ByteArrayOutputStream stream = new ByteArrayOutputStream();
      webServiceMessage1.writeTo(stream);
      if (!stream.toString().contains(phrase)) {
        throw new AssertionError();
      }
    }
  }
}
