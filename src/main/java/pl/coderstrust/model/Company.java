package pl.coderstrust.model;

import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Company {

  private String name;
  private String address;
  private String city;
  private String zipCode;
  private String nip;
  private String bankAccoutNumber;

  public Company() {
  }

  public Company(String name) {
    this.name = name;
  }

  @ApiModelProperty(example = "Company Name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @ApiModelProperty(example = "27 Colmore Row")
  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  @ApiModelProperty(example = "Birmingham")
  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  @ApiModelProperty(example = "B3-2EW")
  public String getZipCode() {
    return zipCode;
  }

  public void setZipCode(String zipCode) {
    this.zipCode = zipCode;
  }

  @ApiModelProperty(example = "7010416384")
  public String getNip() {
    return nip;
  }

  public void setNip(String nip) {
    this.nip = nip;
  }

  @ApiModelProperty(example = "61 1090 1014 0000 0712 1981 2874")
  public String getBankAccoutNumber() {
    return bankAccoutNumber;
  }

  public void setBankAccoutNumber(String bankAccoutNumber) {
    this.bankAccoutNumber = bankAccoutNumber;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
  }

  @Override
  public boolean equals(Object object) {
    return EqualsBuilder.reflectionEquals(this, object);
  }

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this, true);
  }
}
