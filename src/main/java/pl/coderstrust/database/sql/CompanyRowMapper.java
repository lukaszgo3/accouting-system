package pl.coderstrust.database.sql;

import org.springframework.jdbc.core.RowMapper;
import pl.coderstrust.model.Company;
import pl.coderstrust.model.Payment;
import pl.coderstrust.model.TaxType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class CompanyRowMapper implements RowMapper {

  private Payment payment;

  CompanyRowMapper(Payment payment) {
    this.payment = payment;
  }

  CompanyRowMapper() {
  }

  @Override
  public Company mapRow(ResultSet resultSet, int i) throws SQLException {
    Company company = new Company();
    company.setId(resultSet.getInt("id"));
    company.setName(resultSet.getString("companyname"));
    company.setIssueDate(resultSet.getDate("issuedate").toLocalDate());
    company.setAddress(resultSet.getString("address"));
    company.setCity(resultSet.getString("city"));
    company.setZipCode(resultSet.getString("zipcode"));
    company.setNip(resultSet.getString("nip"));
    company.setTaxType(TaxType.valueOf(resultSet.getString("taxtype")));
    company.setBankAccoutNumber(resultSet.getString("bankaccountnumber"));
    company.setPersonalCarUsage(resultSet.getBoolean("caruser"));
    company.setPayments(Arrays.asList(payment));
    return company;
  }
}