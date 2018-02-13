package pl.coderstrust.database.multifile;

import org.junit.Test;

import java.util.List;

public class MultiFileDatabaseTest {

    @Test
    public void addInvoice() {
    }

    @Test
    public void deleteInvoiceById() {
    }

    @Test
    public void getInvoiceById() {
    }

    @Test
    public void updateInvoice() {
    }

    @Test
    public void getInvoices() {
        MultiFileDatabase multiFileDatabase = new MultiFileDatabase();
        List test = multiFileDatabase.getInvoices();
        System.out.println(test.size());

    }
}