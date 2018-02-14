package pl.coderstrust.database.multifile;

import org.junit.Assert;
import org.junit.Test;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

public class FileCasheTest {

    @Test
    public void listFiles() throws FileNotFoundException {
        FileHelper fileHelper = new FileHelper();
        List allFiles = new ArrayList();
        allFiles = fileHelper.listFiles("database");
        System.out.println("Lisf file size " + allFiles.size());
        System.out.println(allFiles.get(0));
    }

    @Test
    public void invoicesCashe() throws FileNotFoundException {
        FileCashe fileCashe = new FileCashe();
        HashMap testMap;
        testMap = fileCashe.invoicesCashe();
        System.out.println("Entries in cashe " + testMap.size());
        System.out.println("Localization " + fileCashe.invoicesCashe().get(1));
        System.out.println(testMap.values());
        System.out.println(testMap.keySet());





    }
}