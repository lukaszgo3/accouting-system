package pl.coderstrust.database.multifile;

import org.junit.Test;
import pl.coderstrust.model.Invoice;

import java.util.List;

public class MultiFileDatabaseTest {

    @Test
    public void addInvoice() {
        MultiFileDatabase multiFileDatabase = new MultiFileDatabase();
        String invoiceToAdd = "{\"systemId\":100,\"visibleId\":\"idVisible_0\",\"buyer\":{\"name\":\"buyer_name_0\",\"address\":\"buyer_address_0\",\"city\":\"buyer_city_0\",\"zipCode\":\"buyer_zipCode_0\",\"nip\":\"buyer_nip_0\",\"bankAccoutNumber\":\"buyer_bankAccoutNumber_0\"},\"seller\":{\"name\":\"seller_name_0\",\"address\":\"seller_address_0\",\"city\":\"seller_city_0\",\"zipCode\":\"seller_zipCode_0\",\"nip\":\"seller_nip_0\",\"bankAccoutNumber\":\"seller_bankAccoutNumber_0\"},\"issueDate\":\"2018-01-01\",\"paymentDate\":\"2018-01-16\",\"products\":[{\"product\":{\"name\":\"name_0_0\",\"description\":\"description_0_0\",\"netValue\":0,\"vatRate\":\"VAT_23\"},\"amount\":0},{\"product\":{\"name\":\"name_0_1\",\"description\":\"description_0_1\",\"netValue\":0,\"vatRate\":\"VAT_23\"},\"amount\":1},{\"product\":{\"name\":\"name_0_2\",\"description\":\"description_0_2\",\"netValue\":0,\"vatRate\":\"VAT_23\"},\"amount\":2}],\"paymentState\":\"NOT_PAID\"}\n";
        multiFileDatabase.addInvoice(multiFileDatabase.jsonToInvoice(invoiceToAdd));
    }

    @Test
    public void deleteInvoiceById() {
    MultiFileDatabase multiFileDatabase = new MultiFileDatabase();
    multiFileDatabase.deleteInvoice((long)3);
    }

    @Test
    public void getInvoiceById() {
        MultiFileDatabase multiFileDatabase = new MultiFileDatabase();
        System.out.println(multiFileDatabase.getInvoiceById((long)5));
    }

    @Test
    public void updateInvoice() {
        Invoice invoice;
        MultiFileDatabase multiFileDatabase = new MultiFileDatabase();
        String invoiceString = "{\"systemId\":2,\"visibleId\":\"idVisible_1\",\"buyer\":{\"name\":\"UPDATED BUYER\",\"address\":\"buyer_address_1\",\"city\":\"buyer_city_1\",\"zipCode\":\"buyer_zipCode_1\",\"nip\":\"buyer_nip_1\",\"bankAccoutNumber\":\"buyer_bankAccoutNumber_1\"},\"seller\":{\"name\":\"seller_name_1\",\"address\":\"seller_address_1\",\"city\":\"seller_city_1\",\"zipCode\":\"seller_zipCode_1\",\"nip\":\"seller_nip_1\",\"bankAccoutNumber\":\"seller_bankAccoutNumber_1\"},\"issueDate\":\"2018-01-01\",\"paymentDate\":\"2018-01-16\",\"products\":[{\"product\":{\"name\":\"name_1_0\",\"description\":\"description_1_0\",\"netValue\":1,\"vatRate\":\"VAT_23\"},\"amount\":0},{\"product\":{\"name\":\"name_1_1\",\"description\":\"description_1_1\",\"netValue\":1,\"vatRate\":\"VAT_23\"},\"amount\":1},{\"product\":{\"name\":\"name_1_2\",\"description\":\"description_1_2\",\"netValue\":1,\"vatRate\":\"VAT_23\"},\"amount\":2},{\"product\":{\"name\":\"name_1_3\",\"description\":\"description_1_3\",\"netValue\":1,\"vatRate\":\"VAT_23\"},\"amount\":3},{\"product\":{\"name\":\"name_1_4\",\"description\":\"description_1_4\",\"netValue\":1,\"vatRate\":\"VAT_23\"},\"amount\":4},{\"product\":{\"name\":\"name_1_5\",\"description\":\"description_1_5\",\"netValue\":1,\"vatRate\":\"VAT_23\"},\"amount\":5},{\"product\":{\"name\":\"name_1_6\",\"description\":\"description_1_6\",\"netValue\":1,\"vatRate\":\"VAT_23\"},\"amount\":6},{\"product\":{\"name\":\"name_1_7\",\"description\":\"description_1_7\",\"netValue\":1,\"vatRate\":\"VAT_23\"},\"amount\":7},{\"product\":{\"name\":\"name_1_8\",\"description\":\"description_1_8\",\"netValue\":1,\"vatRate\":\"VAT_23\"},\"amount\":8},{\"product\":{\"name\":\"name_1_9\",\"description\":\"description_1_9\",\"netValue\":1,\"vatRate\":\"VAT_23\"},\"amount\":9}],\"paymentState\":\"NOT_PAID\"}\n";
        invoice = multiFileDatabase.jsonToInvoice(invoiceString);
        multiFileDatabase.updateInvoice(invoice);
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
