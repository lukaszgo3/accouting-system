package pl.coderstrust.database.multifile;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import pl.coderstrust.model.Invoice;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class FileCashe {

    public FileCashe() throws FileNotFoundException {
    }

    private ObjectMapper objectMapper = new ObjectMapper();

    public static List<File> listf(String directoryName) {
        File dir = new File(directoryName);
        String[] extensions = new String[]{"json"};
        List<File> files = (List<File>) FileUtils.listFiles(dir, extensions, true);
        for (File file : files) {
            try {
                System.out.println("file: " + file.getCanonicalPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return files;
    }

    public HashMap invoicesCashe (){
        List<Invoice> invoices = new ArrayList<>();
        HashMap idCashe = new HashMap();
        List <File> filesCashed = listf("database");
        for (File file: filesCashed) {
            try {
                Invoice invoice = objectMapper.readValue(file, Invoice.class);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return idCashe;
    }
}




