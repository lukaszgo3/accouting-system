package pl.coderstrust.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import pl.coderstrust.configurations.EmailSenderConfig;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.InvoiceEntry;
import pl.coderstrust.model.ProductsInvoicePrices;
import pl.coderstrust.taxservice.TaxCalculatorService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Controller
public class EmailController {

  private final EmailSenderConfig emailSenderConfig;
  private final TemplateEngine templateEngine;
  private BigDecimal totalRetailPrice = BigDecimal.valueOf(0);
  private BigDecimal totalNetValue = BigDecimal.valueOf(0);
  private BigDecimal totalVatValue = BigDecimal.valueOf(0);

  @Autowired
  InvoiceService invoiceService;

  @Autowired
  TaxCalculatorService taxCalculatorService;

  @Autowired
  public EmailController(EmailSenderConfig emailSenderConfig, TemplateEngine templateEngine) {
    this.emailSenderConfig = emailSenderConfig;
    this.templateEngine = templateEngine;
  }

  @RequestMapping("email/{id}")
  public String send(@PathVariable("id") Long invoiceId,
      @RequestParam(name = "email") String email) {
    Invoice invoice = invoiceService.findEntry(invoiceId);

    Context context = new Context();
    context.setVariable("header", "Invoice from GreenTeam");
    context.setVariable("name", "Invoice no. " + invoice.getName());
    context.setVariable("paymentDate", "Payment date: " + invoice.getPaymentDate());
    context.setVariable("issueDate", "Issue date: " + invoice.getIssueDate());
    context.setVariable("sellerName", invoice.getSeller().getName());
    context.setVariable("sellerAddres", invoice.getSeller().getAddress());
    context.setVariable("sellerCity", invoice.getSeller().getCity());
    context.setVariable("sellerZipCode", invoice.getSeller().getZipCode());
    context.setVariable("sellerNip", invoice.getSeller().getNip());
    context.setVariable("sellerBankAccountNumber", invoice.getSeller().getBankAccoutNumber());
    context.setVariable("buyerName", invoice.getBuyer().getName());
    context.setVariable("buyerAddres", invoice.getBuyer().getAddress());
    context.setVariable("buyerCity", invoice.getBuyer().getCity());
    context.setVariable("buyerZipCode", invoice.getBuyer().getZipCode());
    context.setVariable("buyerNip", invoice.getBuyer().getNip());
    context.setVariable("buyerBankAccountNumber", invoice.getBuyer().getBankAccoutNumber());
    context.setVariable("products", pricesList(invoice));
    context.setVariable("totalRetailPrice", totalRetailPrice + "zł");
    context.setVariable("totalNetValue", totalNetValue + "zł");
    context.setVariable("totalVatValue", totalVatValue + "zł");
    context.setVariable("totalPrice", totalNetValue.add(totalVatValue) + "zł");

    String body = templateEngine.process("template", context);
    emailSenderConfig.sendEmail(email, "Invoice from GreenTeam", body);
    return "index";
  }

  private List<ProductsInvoicePrices> pricesList(Invoice invoice) {
    List<InvoiceEntry> products = invoice.getProducts();
    List<ProductsInvoicePrices> pricesList = new ArrayList<>();

    for (InvoiceEntry product : products) {
      BigDecimal netValue = product.getProduct().getNetValue()
          .setScale(2, RoundingMode.HALF_UP);
      double vatRate = product.getProduct().getVatRate().getVatPercent();
      BigDecimal vatValue = netValue.multiply(BigDecimal.valueOf(vatRate))
          .setScale(2, RoundingMode.HALF_UP);
      int amount = product.getAmount();
      BigDecimal netValueMultiplyAmount = netValue.multiply(BigDecimal.valueOf(amount));
      BigDecimal vatValueMultiplyAmount = vatValue.multiply(BigDecimal.valueOf(amount));

      ProductsInvoicePrices invoicePrices = new ProductsInvoicePrices();
      invoicePrices.setProductName(product.getProduct().getName());
      invoicePrices.setAmount(String.valueOf(amount));
      invoicePrices.setRetailPrice(netValue + "zł");
      invoicePrices.setProductNetValue(netValueMultiplyAmount + "zł");
      invoicePrices.setProductVatRate((int) (vatRate * 100) + "%");
      invoicePrices.setProductVatValue(vatValueMultiplyAmount + "zł");
      invoicePrices.setTotal(netValue.add(vatValue).multiply(BigDecimal.valueOf(amount)) + "zł");

      totalRetailPrice = totalRetailPrice.add(netValue);
      totalNetValue = totalNetValue.add(netValueMultiplyAmount);
      totalVatValue = totalVatValue.add(vatValueMultiplyAmount);
      pricesList.add(invoicePrices);
    }
    return pricesList;
  }
}