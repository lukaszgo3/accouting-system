package pl.coderstrust.database.sql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import pl.coderstrust.database.Database;
import pl.coderstrust.model.Company;
import pl.coderstrust.model.Payment;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class CompaniesSqlDb implements Database<Company> {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Override
  public long addEntry(Company entry) {
    CreateSqlTablesIfNotExists tablesIfNotExists = new CreateSqlTablesIfNotExists();
    jdbcTemplate.update(tablesIfNotExists.getCompanyTable());
    jdbcTemplate.update(tablesIfNotExists.getCompanyPaymentTable());

    String companySql =
        "INSERT INTO company (company_name, issue_date, address, city, zip_code, nip, "
            + "bank_account_number, tax_type, caruser) VALUES (?,?,?,?,?,?,?,?,?) RETURNING id ";
    String paymentTypeSql =
        "INSERT INTO companypayment (issue_date, amount, payment_type, id_company)"
            + " VALUES (?,?,?,?)";
    long id = jdbcTemplate.queryForObject(companySql, companyObject(entry), Long.class);

    for (int i = 0; i < entry.getPayments().size(); i++) {
      jdbcTemplate.update(paymentTypeSql,
          companyPaymentObject(entry.getPayments().get(i), id));
    }
    return id;
  }

  @Override
  public void deleteEntry(long id) {
    String deleteQuery = "DELETE FROM company WHERE id =?";
    String deleteCompanyPayment = "DELETE FROM companypayment WHERE id_company=?";
    jdbcTemplate.update(deleteQuery, id);
    jdbcTemplate.update(deleteCompanyPayment, id);
  }

  @Override
  @SuppressWarnings("unchecked")
  public Company getEntryById(long id) {
    String getPayment = "SELECT * FROM companypayment WHERE id_company =?";
    String getQuery = " SELECT * FROM company WHERE id =?";
    List<Payment> payment = jdbcTemplate
        .query(getPayment, new Object[]{id}, new CompanyPaymentRowMapper());

    return (Company) jdbcTemplate
        .queryForObject(getQuery, new Object[]{id}, new CompanyRowMapper(payment));
  }

  @Override
  public void updateEntry(Company entry) {
    long id = entry.getId();
    String paymentTypeSql =
        "INSERT INTO companypayment (issue_date, amount, payment_type, id_company)"
            + " VALUES (?,?,?,?)";
    String insertSql = "UPDATE company set company_name = ?, issue_date = ?, address = ?, city = ?,"
        + " zip_code = ?, nip = ?, bank_account_number = ?, tax_type = ?, caruser = ? WHERE id=";
    String deletePayment = "DELETE FROM companypayment WHERE id_company=?";
    jdbcTemplate.update(deletePayment, id);

    for (int i = 0; i < entry.getPayments().size(); i++) {
      jdbcTemplate.update(paymentTypeSql,
          companyPaymentObject(entry.getPayments().get(i), id));
    }

    jdbcTemplate.update(insertSql + id, companyObject(entry));
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<Company> getEntries() {
    //TODO Company returns null for Payments while getting company by date
    String getQuery = "SELECT * FROM company";
    return jdbcTemplate.query(getQuery, new CompanyRowMapper());
  }

  @Override
  public boolean idExist(long idLong) {
    String getQuery = "SELECT id FROM company WHERE id=";
    return jdbcTemplate.queryForObject(getQuery + idLong, Long.class).equals(idLong);
  }

  private Object[] companyObject(Company entry) {
    Date date = Date.from(entry.getIssueDate().atStartOfDay()
        .atZone(ZoneId.systemDefault()).toInstant());
    return new Object[]{entry.getName(), date, entry.getAddress(), entry.getCity(),
        entry.getZipCode(), entry.getNip(), entry.getBankAccoutNumber(),
        entry.getTaxType().toString(), entry.isPersonalCarUsage()};
  }

  private Object[] companyPaymentObject(Payment entry, long id) {
    Date date = Date.from(entry.getIssueDate().atStartOfDay()
        .atZone(ZoneId.systemDefault()).toInstant());
    return new Object[]{date, entry.getAmount(),
        entry.getType().toString(), id};
  }
}