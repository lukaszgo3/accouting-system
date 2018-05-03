package pl.coderstrust.service.pdfservice;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;

public class PdfFontsProvider {

  private Font headerPropertyFont;
  private Font headerValueFont;
  private Font propertyFont;
  private Font valueFont;


  public PdfFontsProvider() throws Exception{
    BaseFont baseFont = BaseFont.createFont(PdfConfiguration.BASE_FONT_NAME, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
    propertyFont = new Font(baseFont, PdfConfiguration.DEFAULT_FONT_SIZE, Font.BOLD, BaseColor.BLACK);
    valueFont = new Font(baseFont, PdfConfiguration.DEFAULT_FONT_SIZE);
    headerPropertyFont = new Font(baseFont, PdfConfiguration.HEADER_DEFAULT_FONT_SIZE, Font.BOLD, BaseColor.BLACK);
    headerValueFont = new Font(baseFont, PdfConfiguration.HEADER_DEFAULT_FONT_SIZE, Font.NORMAL, BaseColor.BLACK);
  }

  public Font getHeaderPropertyFont() {
    return headerPropertyFont;
  }

  public Font getHeaderValueFont() {
    return headerValueFont;
  }

  public Font getPropertyFont() {
    return propertyFont;
  }

  public Font getValueFont() {
    return valueFont;
  }
}
