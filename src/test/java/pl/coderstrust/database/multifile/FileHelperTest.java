package pl.coderstrust.database.multifile;

import org.junit.Test;

import java.io.IOException;

public class FileHelperTest {

  @Test
  public void addLine() {
  }

  @Test
  public void getAllFilesEntries() throws IOException {

    FileHelper fileHelper = new FileHelper();
    try {
      fileHelper.getAllFilesEntries();
      System.out.println(fileHelper.getAllFilesEntries().get(5));
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println(fileHelper.getAllFilesEntries().size());

  }

  @Test
  public void getLine() throws IOException {
    FileHelper fileHelper = new FileHelper();
    System.out.println(fileHelper.getLine((long) 2));
  }

  @Test
  public void deleteLine() throws IOException {
    FileHelper fileHelper = new FileHelper();
    fileHelper.deleteLine((long) 2);
  }
}