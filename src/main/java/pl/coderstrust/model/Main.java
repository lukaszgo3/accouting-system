package pl.coderstrust.model;

public class Main {
  public static void main(String[] args) {
    Invoice invoice = new InvoiceBuilder(123, "Kupujacy", "sprzedajacu").createInvoice();

    System.out.println(invoice);
  }
}
