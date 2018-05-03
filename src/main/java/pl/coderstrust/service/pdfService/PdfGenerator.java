package pl.coderstrust.service.pdfService;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import pl.coderstrust.model.Company;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.InvoiceEntry;

import java.io.FileOutputStream;
import java.math.BigDecimal;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

public class PdfGenerator {


  private BaseFont bf;
  private Font headerPropertyFont;
  private Font headerValueFont;
  private Font propertyFont;
  private Font valueFont;

  public PdfGenerator() throws Exception {
    bf = BaseFont.createFont("DejaVuSans.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
    propertyFont = new Font(bf, 12, Font.BOLD, BaseColor.BLACK);
    valueFont = new Font(bf, 12);

    headerPropertyFont = new Font(bf, 20, Font.BOLD, BaseColor.BLACK);
    headerValueFont = new Font(bf, 20, Font.NORMAL, BaseColor.BLACK);
  }

  public void invoiceToPdf(Invoice invoice) throws Exception {
    Document document = new Document();
    int margin = 20;
    document.setMargins(margin,margin,margin,margin);
    PdfWriter.getInstance(document, new FileOutputStream("iTextTable.pdf"));

    document.open();

    document.add(getHeader(invoice.getName()));

    document.add(Chunk.NEWLINE);
    document.add(Chunk.NEWLINE);

    document.add(getPropertyValue("ID", Long.toString(invoice.getId())));
    document.add(getPropertyValue("Issue Date", invoice.getIssueDate().toString()));
    document.add(getPropertyValue("Payment Date", invoice.getPaymentDate().toString()));
    document.add(getPropertyValue("Payment State", invoice.getPaymentState().toString()));
    document.add(makeCompanyTable(invoice.getSeller(), invoice.getBuyer()));

    document.add(makeProductsTable(invoice.getProducts()));


    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    Date date = new Date();

    document.add(Chunk.NEWLINE);
    document.add(Chunk.NEWLINE);

    document.add(getPropertyValue("Total value", "0"));
    document.add(getPropertyValue("Invoice generated at", dateFormat.format(date)));
    document.close();
  }

  private PdfPTable makeCompanyTable(Company seller, Company buyer) throws Exception {
    PdfPTable table = new PdfPTable(3);
    addTableHeader(table, Stream.of("Property", "Seller", "Buyer"));

    addRowsCompany(table, "Company name", seller.getName(), buyer.getName());
    addRowsCompany(table, "Company id", Long.toString(seller.getId()),
        Long.toString(buyer.getId()));
    addRowsCompany(table, "Company address", seller.getAddress(), buyer.getAddress());
    addRowsCompany(table, "Company city", seller.getCity(), buyer.getCity());
    addRowsCompany(table, "Company zip code", seller.getZipCode(), buyer.getZipCode());
    addRowsCompany(table, "Company NIP", seller.getNip(), buyer.getNip());
    addRowsCompany(table, "Company BAC", seller.getBankAccoutNumber(), buyer.getBankAccoutNumber());
    table.setSpacingBefore(15f);
    table.setSpacingAfter(15f);
    return table;
  }

  private PdfPTable makeProductsTable(List<InvoiceEntry> products) {
    PdfPTable table = new PdfPTable(6);
    addTableHeader(table,
        Stream.of("Name", "Description", "Type", "Amount", "Net Value", "Vat Rate"));

    for (InvoiceEntry entry : products) {
      addRowsProduct(table, entry);

    }
    return table;
  }


  private void addRowsProduct(PdfPTable table, InvoiceEntry entry) {

    PdfPCell header = new PdfPCell();

    table.addCell(entry.getProduct().getName());
    table.addCell(entry.getProduct().getDescription());
    table.addCell(entry.getProduct().getProductType().getType());
    table.addCell(Integer.toString(entry.getAmount()));
    table.addCell(entry.getProduct().getNetValue().setScale(4,BigDecimal.ROUND_HALF_UP).toString());
    table.addCell(entry.getProduct().getVatRate().toString());
  }


  private void addRowsCompany(PdfPTable table, String caption, String sellerValue,
      String buyerValue) {

    PdfPCell header = new PdfPCell();
    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
    //header.setBorderWidth(1);
    header.setPhrase(new Phrase(caption));
    header.setHorizontalAlignment(Element.ALIGN_CENTER);
    header.setVerticalAlignment(Element.ALIGN_MIDDLE);

    table.addCell(header);

    table.addCell(sellerValue);
    table.addCell(buyerValue);

  }
  private Paragraph getHeader(String invoiceName) {
    Chunk myChunk = new Chunk("Invoice: ", headerPropertyFont);
    Paragraph paragraph = new Paragraph();
    paragraph.add(myChunk);

    Chunk myChunk2 = new Chunk(invoiceName, headerValueFont);
    paragraph.add(myChunk2);

    paragraph.setAlignment(Element.ALIGN_CENTER);
    return paragraph;
  }
  private Paragraph getPropertyValue(String property, String value) {
    Chunk myChunk = new Chunk(property + ": ", propertyFont);
    Paragraph paragraph = new Paragraph();
    paragraph.add(myChunk);

    Chunk myChunk2 = new Chunk(value, valueFont);
    paragraph.add(myChunk2);

    paragraph.setAlignment(Element.ALIGN_LEFT);
    return paragraph;
  }
  private void addTableHeader(PdfPTable table, Stream<String> headers) {
    headers
        .forEach(columnTitle -> {
          PdfPCell header = new PdfPCell();
          header.setBackgroundColor(BaseColor.LIGHT_GRAY);
          header.setPhrase(new Phrase(columnTitle, propertyFont));
          header.setHorizontalAlignment(Element.ALIGN_CENTER);
          header.setVerticalAlignment(Element.ALIGN_MIDDLE);
          table.addCell(header);
        });
  }
}
