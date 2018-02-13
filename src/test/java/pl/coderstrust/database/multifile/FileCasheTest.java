package pl.coderstrust.database.multifile;

import org.junit.Test;

import java.io.FileNotFoundException;

public class FileCasheTest {

  @Test
  public void listf() throws FileNotFoundException {
      FileCashe fileCashe = new FileCashe();
      System.out.println(fileCashe.invoicesCashe().size());


  }
}