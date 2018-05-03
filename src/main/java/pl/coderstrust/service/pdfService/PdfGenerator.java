package pl.coderstrust.service.pdfService;

import static com.itextpdf.text.html.HtmlTags.FONT;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.stream.Stream;

public class PdfGenerator {


  private BaseFont bf;
  private Font headerPropertyFont;
  private  Font headerValueFont;
  private  Font propertyFont;
  private  Font valueFont;

  public Table() throws Exception {
    bf = BaseFont.createFont("DejaVuSans.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
    propertyFont = new Font(bf,12,Font.BOLD, BaseColor.BLACK);
    valueFont = new Font(bf, 12);

    headerPropertyFont = new Font(bf,20,Font.BOLD, BaseColor.BLACK);;
    headerValueFont = new Font(bf,20,Font.NORMAL, BaseColor.BLACK);
  }

  public  void generatePdf() throws Exception {
    Document document = new Document();
    PdfWriter.getInstance(document, new FileOutputStream("iTextTable.pdf"));

    document.open();

    document.add(getHeader("Sample invoice No 1.2.2.3"));

    document.add( Chunk.NEWLINE );
    document.add( Chunk.NEWLINE );

    document.add(getPropertyValue("Creation Date","2017-01-01"));
    document.add(getPropertyValue("Payment Date","2017-02-01"));
    document.add(getPropertyValue("Payment State","NOT PAID"));


    PdfPTable table = new PdfPTable(3);
    addTableHeader(table);
    addRows(table);


    table.setSpacingBefore(15f);
    table.setSpacingAfter(15f);
    document.add(table);

    document.close();
  }

  private Paragraph getHeader(String invoiceName){
    Chunk myChunk = new Chunk("Invoice: ",headerPropertyFont);
    Paragraph paragraph = new Paragraph();
    paragraph.add(myChunk);

    Chunk myChunk2 = new Chunk(invoiceName, headerValueFont);
    paragraph.add(myChunk2);

    paragraph.setAlignment(Element.ALIGN_CENTER);
    return paragraph;
  }

  private  Paragraph getPropertyValue(String property, String value){
    Chunk myChunk = new Chunk(property+": ",propertyFont);
    Paragraph paragraph = new Paragraph();
    paragraph.add(myChunk);

    Chunk myChunk2 = new Chunk(value, valueFont);
    paragraph.add(myChunk2);

    paragraph.setAlignment(Element.ALIGN_LEFT);
    return paragraph;
  }

  private  void addTableHeader(PdfPTable table) {

    Stream.of("Data", "Seller", "Buyer")
        .forEach(columnTitle -> {
          PdfPCell header = new PdfPCell();
          header.setBackgroundColor(BaseColor.LIGHT_GRAY);
          header.setPhrase(new Phrase(columnTitle,propertyFont));
          header.setHorizontalAlignment(Element.ALIGN_CENTER);
          header.setVerticalAlignment(Element.ALIGN_MIDDLE);
          table.addCell(header);
        });
  }

  private  void addRows(PdfPTable table) throws  Exception {

    PdfPCell header = new PdfPCell();
    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
    //header.setBorderWidth(1);
    header.setPhrase(new Phrase("Name"));
    header.setHorizontalAlignment(Element.ALIGN_CENTER);
    header.setVerticalAlignment(Element.ALIGN_MIDDLE);

    table.addCell(header);


    table.addCell("Firma xyz");
    table.addCell("Firma ABC");



    PdfPCell header2 = new PdfPCell();
    header2.setBackgroundColor(BaseColor.LIGHT_GRAY);
    // header2.setBorderWidth(1);
    header2.setPhrase(new Phrase("Address"));
    header2.setHorizontalAlignment(Element.ALIGN_CENTER);
    header2.setVerticalAlignment(Element.ALIGN_MIDDLE);

    table.addCell(header2);

    table.addCell("Ulica przykladowa 2222 Miescowosc1 9494-3939 Polska");
    table.addCell("Ulica przykladowa 2222 Miejscowość 9494-3939 Polska ŁŹĆ");



    PdfPCell header3 = new PdfPCell();
    header3.setBackgroundColor(BaseColor.LIGHT_GRAY);
    // header3.setBorderWidth(1);
    header3.setPhrase(new Phrase("NIP"));
    header3.setHorizontalAlignment(Element.ALIGN_CENTER);
    header3.setVerticalAlignment(Element.ALIGN_MIDDLE);

    table.addCell(header3);


    table.addCell("NIP");
    table.addCell("6464968496849684");
    table.addCell("6465464646546545");

    table.addCell(getNormalCell("row 1, col 3",null,5));

  }


  public static PdfPCell getNormalCell(String string, String language, float size)
      throws DocumentException, IOException {
    if(string != null && "".equals(string)){
      return new PdfPCell();
    }
    Font f  = getFontForThisLanguage(language);
    if(size < 0) {
      f.setColor(BaseColor.RED);
      size = -size;
    }
    f.setSize(size);
    PdfPCell cell = new PdfPCell(new Phrase(string, f));
    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
    return cell;
  }

  public static Font getFontForThisLanguage(String language) {
    if ("czech".equals(language)) {
      return FontFactory.getFont(FONT, "Cp1250", true);
    }
    if ("greek".equals(language)) {
      return FontFactory.getFont(FONT, "Cp1253", true);
    }
    return FontFactory.getFont(FONT, null, true);
  }


}
