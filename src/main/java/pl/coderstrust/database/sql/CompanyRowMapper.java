package pl.coderstrust.database.sql;

import org.springframework.jdbc.core.RowMapper;
import pl.coderstrust.model.Company;
import pl.coderstrust.model.Payment;
import pl.coderstrust.model.TaxType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CompanyRowMapper implements RowMapper {

  private List<Payment> payment;

  CompanyRowMapper(List<Payment> payment) {
    this.payment = payment;
  }

  CompanyRowMapper() {
  }

  @Override
  public Company mapRow(ResultSet resultSet, int i) throws SQLException {
    Company company = new Company();

    if (!resultSet.wasNull()) {
      company.setId(resultSet.getInt("id"));
      company.setName(resultSet.getString("company_name"));
      company.setIssueDate(resultSet.getDate("issue_date").toLocalDate());
      company.setAddress(resultSet.getString("address"));
      company.setCity(resultSet.getString("city"));
      company.setZipCode(resultSet.getString("zip_code"));
      company.setNip(resultSet.getString("nip"));
      company.setTaxType(TaxType.valueOf(resultSet.getString("tax_type")));
      company.setBankAccoutNumber(resultSet.getString("bank_account_number"));
      company.setPersonalCarUsage(resultSet.getBoolean("caruser"));
      company.setPayments(payment);
    }
    return company;
  }
}