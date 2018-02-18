package pl.coderstrust.database.multifile;

import com.fasterxml.jackson.core.JsonProcessingException;
import pl.coderstrust.database.Database;
import pl.coderstrust.database.database.memory.ObjectMapperProvider;
import pl.coderstrust.model.Invoice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MultiFileDatabase implements Database {

    private ObjectMapperProvider objectMapper;
    private FileHelper fileHelper;
    private Invoice invoice;
    FileCache fileCache;
    PathSelector pathSelector;

    public MultiFileDatabase() {
        objectMapper = new ObjectMapperProvider();
        fileHelper = new FileHelper();
        invoice = new Invoice();
        fileCache = new FileCache();
        pathSelector = new PathSelector();
    }


    @Override
    public void addInvoice(Invoice invoice) {
        if (fileCache.cashe.containsKey(invoice.getSystemId())) {
            System.out.println("Failed, invoice with this ID exist in databese");
        } else
            try {
                fileHelper.addLine(objectMapper.toJson(invoice), invoice);
                fileCache.cashe.put(invoice.getSystemId(), pathSelector.getFilePath(invoice));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
    }

    @Override
    public void deleteInvoice(long id) {
        if (fileCache.cashe.containsKey(id)) {
            try {
                fileHelper.deleteLine(id);
            } catch (IOException e) {
                e.printStackTrace();
            }
            fileCache.cashe.remove(id);
        } else {
            System.out.println("Invoice with id " + id + " does not exist");
        }
    }


    @Override
    public Invoice getInvoiceById(long id) {
        if (fileCache.cashe.containsKey(id)) {
            try {
                invoice = objectMapper.toInvoice(fileHelper.getLine(id));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Invoice with id " + id + " does not exist");
        }
        return invoice;
    }

    @Override
    public void updateInvoice(Invoice invoice) {
        if (fileCache.cashe.containsKey(invoice.getSystemId())) {
            deleteInvoice(invoice.getSystemId());
            addInvoice(invoice);
        }
    }


    @Override
    public List<Invoice> getInvoices() {

        List<Invoice> invoices = new ArrayList<>();
        ArrayList<String> linesFromAllFiles;
        try {
            linesFromAllFiles = fileHelper.getAllFilesEntries();
            for (int i = 0; i < linesFromAllFiles.size(); i++) {
                invoices.add(objectMapper.toInvoice(linesFromAllFiles.get(i)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return invoices;
    }

    @Override
    public boolean idExist(long id) {
        boolean idCheck;
        if (fileCache.cashe.containsKey(id)) {
        idCheck = true;}
        else {
            idCheck = false;
        }
        return idCheck;
    }

    Invoice jsonToInvoice(String json) {
        Invoice invoice = null;
        try {
            invoice = objectMapper.toInvoice(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return invoice;
    }
}