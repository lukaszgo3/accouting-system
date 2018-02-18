package pl.coderstrust.database.multifile;

import org.junit.Test;
import pl.coderstrust.model.Invoice;

import java.util.List;

public class MultiFileDatabaseTest {

    @Test
    public void addInvoice() {
    }

    @Test
    public void deleteInvoiceById() {
    MultiFileDatabase multiFileDatabase = new MultiFileDatabase();
    multiFileDatabase.deleteInvoice((long)3);
    }

    @Test
    public void getInvoiceById() {
        MultiFileDatabase multiFileDatabase = new MultiFileDatabase();
        System.out.println(multiFileDatabase.getInvoiceById((long)2));
    }

    @Test
    public void updateInvoice() {
    }

    @Test
    public void getInvoices() {
        MultiFileDatabase multiFileDatabase = new MultiFileDatabase();
        FileCache fileCache = new FileCache();
        List<Invoice> test = multiFileDatabase.getInvoices();
        System.out.println(test.size());
        System.out.println(fileCache.cashe.size());
        for (Invoice i:test) {
            System.out.println(i);
        }

        }

    }
