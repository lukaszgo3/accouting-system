package pl.coderstrust.database.multifile;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FileCacheTest {

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
        FileCache fileCache = new FileCache();
        HashMap testMap;
        testMap = fileCache.invoicesCache();
        System.out.println("Entries in cashe " + testMap.size());
        System.out.println("Localization " + fileCache.invoicesCache().get((long)1));
        System.out.println(testMap.values());
        System.out.println(testMap.keySet());
        System.out.println(testMap.get((long)1));
        System.out.println(testMap.containsKey((long)1));





    }
}