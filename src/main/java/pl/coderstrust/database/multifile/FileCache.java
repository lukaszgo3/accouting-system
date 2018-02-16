package pl.coderstrust.database.multifile;

import pl.coderstrust.database.database.memory.ObjectMapperProvider;
import pl.coderstrust.model.Invoice;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class FileCache {

    private FileHelper fileHelper = new FileHelper();
    private ObjectMapperProvider objectMapper = new ObjectMapperProvider();
    public HashMap cashe = invoicesCache();


    public HashMap invoicesCache() {
        List<File> files = fileHelper.listFiles("database");
        HashMap idCache = new HashMap();
        Invoice invoice;
        List<Invoice> invoices = new ArrayList<>();
        String line = null;
        for (int i = 0; i < files.size(); i++) {
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(files.get(i)));
                while ((line = bufferedReader.readLine()) != null) {
                    invoice = jsonToInvoice(line);
                    idCache.put(invoice.getSystemId(), files.get(i));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return idCache;
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