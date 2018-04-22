//package pl.coderstrust.service.soap;
//
//import org.springframework.stereotype.Service;
//import pl.coderstrust.model.Invoice;
//
//import javax.xml.bind.*;
//import javax.xml.namespace.QName;
//import java.io.StringWriter;
//import java.util.List;
//
//@Service
//public class JaxbXmlHandler {
//
//    private Marshaller jaxbMarshallerInvoice;
//    private Marshaller jaxbMarshallerInvoiceList;
//    private StringWriter sw;
//    private model.soap.InvoiceList invoiceList;
//    ModelSoapConverter converter;
//
//    public JaxbXmlHandler() {
//        try {
//
//            JAXBContext jaxbContext = JAXBContext.newInstance(model.soap.Invoice.class);
//            jaxbMarshallerInvoice = jaxbContext.createMarshaller();
//            jaxbMarshallerInvoice.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//
//            jaxbContext =  JAXBContext.newInstance(model.soap.InvoiceList.class);
//            jaxbMarshallerInvoiceList = jaxbContext.createMarshaller();
//            jaxbMarshallerInvoiceList.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//
//            sw = new StringWriter();
//            invoiceList = new model.soap.InvoiceList();
//            converter = new ModelSoapConverter();
//
//        } catch (JAXBException e) {
//            e.printStackTrace();
//            //TODO: take care about this exceptio
//        }
//    }
//
//    public String marshall(Invoice invoice) {
//        try {
//            jaxbMarshallerInvoice.marshal(
//                    new JAXBElement<model.soap.Invoice>(
//                            new QName(null,""),model.soap.Invoice.class, converter.invoiceToSoapInvoice(invoice)), sw);
//
//            //marshaller.marshal(new JAXBElement<MyClass>(new QName("uri","local"), MyClass.class, myClassInstance), System.out);
//            return sw.toString();
//        } catch (JAXBException e) {
//            //TODO: take care about this exception
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public String marshall(List<Invoice> invoices) {
//
//        for (Invoice invoice: invoices) {
//            invoiceList.getInv().add(converter.invoiceToSoapInvoice(invoice));
//        }
//
//
//        try {
//            jaxbMarshallerInvoice.marshal(invoiceList, sw);
//        } catch (JAXBException e) {
//            e.printStackTrace();
//        }
//        return sw.toString();
//    }
//
////        public static List<Book> unmarshal(File importFile) throws JAXBException {
////            Books books = new Books();
////
////            JAXBContext context = JAXBContext.newInstance(Books.class);
////            Unmarshaller um = context.createUnmarshaller();
////            books = (Books) um.unmarshal(importFile);
////
////            return books.getBooks();
////        }
////    }
////
////    @XmlAccessorType(XmlAccessType.FIELD)
////    @XmlRootElement(name = "invoices")
////    private class InvoiceItems {
////        private List<Invoice> invoices = new ArrayList<>();
////
////        public InvoiceItems() {
////        }
////
////        public InvoiceItems(List<Invoice> invoices) {
////            this.invoices = invoices;
////        }
////
////        public List<Invoice> getInvoices() {
////            return invoices;
////        }
////
////        public void setInvoices(List<Invoice> invoices) {
////            this.invoices = invoices;
////        }
////    }
//}
