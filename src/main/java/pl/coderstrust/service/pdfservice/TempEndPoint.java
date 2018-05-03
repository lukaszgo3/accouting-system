package pl.coderstrust.service.pdfservice;

import pl.coderstrust.model.Invoice;

public class TempEndPoint {

  public static void main(String[] args) throws Exception {
    PdfGenerator gen = new PdfGenerator();
    TestCasesGenerator gene = new TestCasesGenerator();
    Invoice inv = gene.getTestInvoice(1, 16);
    gen.invoiceToPdf(inv);


  }

}
