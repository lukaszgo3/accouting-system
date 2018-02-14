package pl.coderstrust.database.multifile;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import pl.coderstrust.database.database.memory.ObjectMapperProvider;
import pl.coderstrust.model.Invoice;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class FileCashe {

    private FileHelper fileHelper = new FileHelper();
    private ObjectMapperProvider objectMapper = new ObjectMapperProvider();
    public HashMap cashe = invoicesCashe();


    public HashMap invoicesCashe() {
        List<File> files = fileHelper.listFiles("database");
        HashMap idCashe = new HashMap();
        Invoice invoice;
        List<Invoice> invoices = new ArrayList<>();
        String line = null;
        for (int i = 0; i < files.size(); i++) {
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(files.get(i)));
                while ((line = bufferedReader.readLine()) != null) {
                    invoice = jsonToInvoice(line);
                    idCashe.put(invoice.getSystemId(), files.get(i));
                    System.out.println(invoice.getSystemId());
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return idCashe;
    }

    Invoice jsonToInvoice (String json){
        Invoice invoice = null;
        try {
            invoice= objectMapper.toInvoice(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return invoice;
    }
}