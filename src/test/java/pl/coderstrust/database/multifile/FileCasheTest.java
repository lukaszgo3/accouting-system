package pl.coderstrust.database.multifile;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class FileCasheTest {

  @Test
  public void listf() throws FileNotFoundException {
      FileCashe fileCashe = new FileCashe();
      System.out.println(fileCashe.invoicesCashe().size());
      List allFiles = new ArrayList();
      allFiles = fileCashe.listf("database");
      System.out.println("Lisf file size " + allFiles.size());
      System.out.println(allFiles.get(1));



  }
}