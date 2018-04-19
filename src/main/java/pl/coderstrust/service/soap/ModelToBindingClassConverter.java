package pl.coderstrust.service.soap;

import model.soap.GetInvoicePostRequest;
import model.soap.InvoiceAddRequest;
import pl.coderstrust.model.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ModelToBindingClassConverter {

    public Invoice soapInvoiceToInvoice(InvoiceAddRequest request) {
        model.soap.Invoice invoice = request.getInvoice();
        Company seller = soapCompanyToCompany(invoice.getSeller());
        Company buyer = soapCompanyToCompany(invoice.getBuyer());
        InvoiceBuilder builder = new InvoiceBuilder(invoice.getId(), buyer.getName(), seller.getName());
        builder.setVisibleId(invoice.getName());
        builder.setBuyer(buyer);
        builder.setSeller(seller);
        builder.setIssueDate(LocalDate.parse(invoice.getInvoiceIssueDate()));
        builder.setPaymentDate(LocalDate.parse(invoice.getPaymentDate()));
        builder.setProducts(soapEntriesToEntries(invoice.getProducts()));
        builder.setPaymentState(PaymentState.valueOf(invoice.getPaymentState().value()));
        return builder.build();
    }

    public model.soap.Invoice invoiceToSoapInvoice(Invoice invoice) {
        model.soap.Invoice soapInvoice = new model.soap.Invoice();
        soapInvoice.setId(invoice.getId());
        soapInvoice.setName(invoice.getName());
        soapInvoice.setPaymentDate(invoice.getPaymentDate().toString());
        soapInvoice.setPaymentState(model.soap.PaymentState.fromValue(invoice.getPaymentState().toString()));
        soapInvoice.setBuyer(companyToSoapCompany(invoice.getBuyer()));
        soapInvoice.setSeller(companyToSoapCompany(invoice.getSeller()));

        for (InvoiceEntry product : invoice.getProducts()) {
            soapInvoice.getProducts().add(invoiceEntryToSoapInvoiceEntry(product));
        }
        return soapInvoice;
    }

    private model.soap.InvoiceEntry invoiceEntryToSoapInvoiceEntry(InvoiceEntry entry) {
        model.soap.InvoiceEntry soapInvoiceEntry = new model.soap.InvoiceEntry();
        soapInvoiceEntry.setAmount(entry.getAmount());
        soapInvoiceEntry.setProduct(productToSoapProduct(entry.getProduct()));
        return soapInvoiceEntry;
    }

    private model.soap.Product productToSoapProduct(Product entry) {
        model.soap.Product soapProduct = new model.soap.Product();
        soapProduct.setDescription(entry.getDescription());
        soapProduct.setName(entry.getName());
        soapProduct.setNetValue(entry.getNetValue());
        soapProduct.setProductType(model.soap.ProductType.fromValue(entry.getProductType().toString()));
        soapProduct.setVatRate(model.soap.Vat.valueOf(entry.getVatRate().toString()));
        return soapProduct;
    }


    private model.soap.Company companyToSoapCompany(Company company) {
        model.soap.Company soapCompany = new model.soap.Company();
        soapCompany.setAddress(company.getAddress());
        soapCompany.setBankAccoutNumber(company.getBankAccoutNumber());
        soapCompany.setCity(company.getCity());
        soapCompany.setCustomerIssueDate(company.getIssueDate().toString());
        soapCompany.setId(company.getId());
        soapCompany.setName(company.getName());
        soapCompany.setNip(company.getName());
        soapCompany.setZipCode(company.getZipCode());
        //soapCompany.setPersonalCarUsage(company.get);
        soapCompany.setTaxType(model.soap.TaxType.valueOf(company.getTaxType().toString()));
        return soapCompany;
    }


    private Company soapCompanyToCompany(model.soap.Company company) {
        CompanyBuilder builder = new CompanyBuilder(company.getName());
        builder.setIssueDate(LocalDate.parse(company.getCustomerIssueDate()));
        builder.setAddress(company.getAddress());
        builder.setCity(company.getCity());
        builder.setZipCode(company.getZipCode());
        builder.setNip(company.getNip());
        builder.setBankAccoutNumber(company.getBankAccoutNumber());
        builder.setTaxType(TaxType.valueOf(company.getTaxType().value()));
        builder.setIsCarPersonalUsage(company.isPersonalCarUsage());
        return builder.build();
    }


    private List<InvoiceEntry> soapEntriesToEntries(List<model.soap.InvoiceEntry> entries) {
        int productsCount = entries.size();
        ArrayList<InvoiceEntry> invoiceEntries = new ArrayList<>(productsCount);

        for (model.soap.InvoiceEntry entry : entries) {
            invoiceEntries.add(new InvoiceEntry(soapProductToProduct(entry.getProduct()), entry.getAmount()));
        }
        return invoiceEntries;
    }

    private Product soapProductToProduct(model.soap.Product product) {
        ProductBuilder builder = new ProductBuilder(product.getName(), product.getNetValue().doubleValue());
        builder.setDescription(product.getDescription());
        builder.setVatRate(Vat.valueOf(product.getVatRate().value()));
        builder.setProductType(ProductType.valueOf(product.getProductType().value()));
        return builder.build();
    }
}
