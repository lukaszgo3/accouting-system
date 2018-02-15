package pl.coderstrust.model;

public class Company {

  private String name;
  private String address;
  private String city;
  private String zipCode;
  private String nip;
  private String bankAccoutNumber;

  private Company() {
  }

  public Company(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getZipCode() {
    return zipCode;
  }

  public void setZipCode(String zipCode) {
    this.zipCode = zipCode;
  }

  public String getNip() {
    return nip;
  }

  public void setNip(String nip) {
    this.nip = nip;
  }

  public String getBankAccoutNumber() {
    return bankAccoutNumber;
  }

  public void setBankAccoutNumber(String bankAccoutNumber) {
    this.bankAccoutNumber = bankAccoutNumber;
  }

  @Override
  public String toString() {
    return "Company{"
        + "name='" + name + '\''
        + ", address='" + address + '\''
        + ", city='" + city + '\''
        + ", zipCode='" + zipCode + '\''
        + ", nip='" + nip + '\''
        + ", bankAccoutNumber='" + bankAccoutNumber + '\'' + '}';
  }

  public static class Builder {
    private String name;
    private String address;
    private String city;
    private String zipCode;
    private String nip;
    private String bankAccoutNumber;

    public Builder(String name) {
      this.name = name;
    }

    public Builder setAddress(String address) {
      this.address = address;
      return this;
    }

    public Builder setCity(String city) {
      this.city = city;
      return this;
    }

    public Builder setZipCode(String zipCode) {
      this.zipCode = zipCode;
      return this;
    }

    public Builder setNip(String nip) {
      this.nip = nip;
      return this;
    }

    Builder setBankAccoutNumber(String bankAccoutNumber) {
      this.bankAccoutNumber = bankAccoutNumber;
      return this;
    }

    public Company createCompany() {
      Company company = new Company();
      company.name = this.name;
      company.address = this.address;
      company.city = this.city;
      company.nip = this.nip;
      company.zipCode = this.zipCode;
      company.bankAccoutNumber = this.bankAccoutNumber;
      return company;
    }
  }
}