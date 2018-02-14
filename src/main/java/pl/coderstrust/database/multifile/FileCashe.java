package pl.coderstrust.database.multifile;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import pl.coderstrust.model.Invoice;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class FileCashe {

    private MultiFileDatabase multiFileDatabase = new MultiFileDatabase();
    
    public FileCashe() {
    }

    public List<File> listFiles(String directoryName) {
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

    public HashMap invoicesCashe() {
        List<File> files = listFiles("database");
        HashMap idCashe = new HashMap();
        Invoice invoice;
        List<Invoice> invoices = new ArrayList<>();
        String line = null;
        for (int i = 0; i < files.size(); i++) {
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(files.get(i)));
                while ((line = bufferedReader.readLine()) != null) {
                    invoice = multiFileDatabase.jsonToInvoice(line);
                    idCashe.put(invoice.getSystemId(), files.get(i));
                    System.out.println(invoice.getSystemId());
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return idCashe;
    }
}