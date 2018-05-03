package pl.coderstrust.service.pdfservice;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import pl.coderstrust.model.Company;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.InvoiceEntry;

import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

public class PdfGenerator {

  private Document document;
  private PdfFontsProvider fonts = new PdfFontsProvider();
  private DateFormat dateFormat;

  public PdfGenerator() throws Exception {
    document = new Document();

    document.setMargins(
        PdfConfiguration.DEFAULT_MARGIN_SIZE,
        PdfConfiguration.DEFAULT_MARGIN_SIZE,
        PdfConfiguration.DEFAULT_MARGIN_SIZE,
        PdfConfiguration.DEFAULT_MARGIN_SIZE
    );

    PdfWriter.getInstance(document, new FileOutputStream(PdfConfiguration.TEMP_FILE_NAME));
    dateFormat = new SimpleDateFormat(PdfConfiguration.DATE_FORMAT);
    document.open();
  }

  public void invoiceToPdf(Invoice invoice) throws Exception {
    document.add(getInvoiceHeaderParagraph(invoice.getName()));
    document.add(Chunk.NEWLINE);
    document.add(getPropertyValueParagraph("ID", Long.toString(invoice.getId())));
    document.add(getPropertyValueParagraph("Issue Date", invoice.getIssueDate().toString()));
    document.add(getPropertyValueParagraph("Payment Date", invoice.getPaymentDate().toString()));
    document.add(getPropertyValueParagraph("Payment State", invoice.getPaymentState().toString()));
    document.add(getCompanyTable(invoice.getSeller(), invoice.getBuyer()));
    document.add(getProductsTable(invoice.getProducts()));
    document.add(Chunk.NEWLINE);
    document.add(getPropertyValueParagraph("Invoice generated at", dateFormat.format(new Date())));
    document.close();
  }

  private Paragraph getInvoiceHeaderParagraph(String invoiceName) {
    Paragraph paragraph = new Paragraph();
    paragraph.setAlignment(Element.ALIGN_CENTER);
    paragraph.add(new Chunk("Invoice: ", fonts.getHeaderPropertyFont()));
    paragraph.add(new Chunk(invoiceName, fonts.getHeaderValueFont()));
    return paragraph;
  }

  private Paragraph getPropertyValueParagraph(String property, String value) {
    Paragraph paragraph = new Paragraph();
    paragraph.setAlignment(Element.ALIGN_LEFT);
    paragraph.add(new Chunk(property + ": ", fonts.getPropertyFont()));
    paragraph.add(new Chunk(value, fonts.getValueFont()));
    return paragraph;
  }


  private PdfPTable getCompanyTable(Company seller, Company buyer)  {
    PdfPTable table = new PdfPTable(PdfConfiguration.COMPANIES_TABLE_COLUMNS_COUNT);
    table.setSpacingBefore(PdfConfiguration.TABLE_SPACING);
    table.setSpacingAfter(PdfConfiguration.TABLE_SPACING);

    addTableHeader(table, PdfConfiguration.COMPANIES_TABLE_HEADERS);

    addRowsCompanyTable(table, "Company name", seller.getName(), buyer.getName());
    addRowsCompanyTable(table, "Company id", Long.toString(seller.getId()),
        Long.toString(buyer.getId()));
    addRowsCompanyTable(table, "Company address", seller.getAddress(), buyer.getAddress());
    addRowsCompanyTable(table, "Company city", seller.getCity(), buyer.getCity());
    addRowsCompanyTable(table, "Company zip code", seller.getZipCode(), buyer.getZipCode());
    addRowsCompanyTable(table, "Company NIP", seller.getNip(), buyer.getNip());
    addRowsCompanyTable(table, "Company BAC", seller.getBankAccoutNumber(), buyer.getBankAccoutNumber());

    return table;
  }

  private void addTableHeader(PdfPTable table, Stream<String> headers) {
    headers
        .forEach(columnTitle -> {
          table.addCell(getHeaderCell(columnTitle));
        });
  }

  private PdfPCell getHeaderCell(String content) {
    PdfPCell cell = new PdfPCell();
    cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
    cell.setPhrase(new Phrase(content, fonts.getPropertyFont()));
    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

    return cell;
  }

  private void addRowsCompanyTable(PdfPTable table, String caption, String sellerValue,
      String buyerValue) {

    table.addCell(getHeaderCell(caption));
    table.addCell(sellerValue);
    table.addCell(buyerValue);

  }

  private PdfPTable getProductsTable(List<InvoiceEntry> products) {
    PdfPTable table = new PdfPTable(PdfConfiguration.PRODUCTS_TABLE_COLUMNS_COUNT);
    table.setSpacingBefore(PdfConfiguration.TABLE_SPACING);
    table.setSpacingAfter(PdfConfiguration.TABLE_SPACING);

    addTableHeader(table, PdfConfiguration.PRODUCTS_TABLE_HEADERS);

    for (InvoiceEntry entry : products) {
      addRowsProductTable(table, entry);
    }
    return table;
  }


  private void addRowsProductTable(PdfPTable table, InvoiceEntry entry) {
    table.addCell(entry.getProduct().getName());
    table.addCell(entry.getProduct().getDescription());
    table.addCell(entry.getProduct().getProductType().getType());
    table.addCell(Integer.toString(entry.getAmount()));
    table.addCell(entry.getProduct().getNetValue()
        .setScale(PdfConfiguration.ROUND_DIGITS_BIG_DECIMAL,
            PdfConfiguration.ROUND_MODE_BIG_DECIMAL).toString());
    table.addCell(entry.getProduct().getVatRate().toString());
  }

}
