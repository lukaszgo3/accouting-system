package pl.coderstrust.service.pdfservice;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import java.io.IOException;

public class pdfReader {

  private static final String FILE_NAME = "/tmp/itext.pdf";

  public static void main(String[] args) {

    PdfReader reader;

    try {

      reader = new PdfReader(
          "/home/kmalysiak/WORK/project-4-seba-seba-krzysiek-lukasz/iTextTable.pdf");

      // pageNumber = 1
      String textFromPage = PdfTextExtractor.getTextFromPage(reader, 1);

      System.out.println(textFromPage);

      reader.close();

    } catch (IOException e) {
      e.printStackTrace();
    }

  }

}
