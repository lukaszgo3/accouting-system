package pl.coderstrust.model;

public class Company {

  private String name;
  private String address;
  private String city;
  private String zipCode;
  private String nip;
  private String bankAccoutNumber;

  /**
   * Constructor.
   */
  public Company(String name, String address, String city, String zipCode, String nip,
      String bankAccoutNumber) {
    this.name = name;
    this.address = address;
    this.city = city;
    this.zipCode = zipCode;
    this.nip = nip;
    this.bankAccoutNumber = bankAccoutNumber;
  }
}
