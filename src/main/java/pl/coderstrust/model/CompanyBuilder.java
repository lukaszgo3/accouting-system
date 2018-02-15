package pl.coderstrust.model;

public class CompanyBuilder {
  private String name;
  private String address;
  private String city;
  private String zipCode;
  private String nip;
  private String bankAccoutNumber;

  public CompanyBuilder(String name) {
    this.name = name;
  }

  public CompanyBuilder setAddress(String address) {
    this.address = address;
    return this;
  }

  public CompanyBuilder setCity(String city) {
    this.city = city;
    return this;
  }

  public CompanyBuilder setZipCode(String zipCode) {
    this.zipCode = zipCode;
    return this;
  }

  public CompanyBuilder setNip(String nip) {
    this.nip = nip;
    return this;
  }

  public CompanyBuilder setBankAccoutNumber(String bankAccoutNumber) {
    this.bankAccoutNumber = bankAccoutNumber;
    return this;
  }

  public Company createCompany() {
    Company company = new Company();
    company.setName(this.name);
    company.setAddress(this.address);
    company.setCity(this.city);
    company.setNip(this.nip);
    company.setZipCode(this.zipCode);
    company.setBankAccoutNumber(this.bankAccoutNumber);
    return company;
  }
}