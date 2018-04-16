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

  public CompaniesSqlDb(Class<Company> companyClass) {
  }

  @Override
  public long addEntry(Company entry) {
    CreateSqlTablesIfNotExists tablesIfNotExists = new CreateSqlTablesIfNotExists();
    jdbcTemplate.update(tablesIfNotExists.getCompanyTable());
    jdbcTemplate.update(tablesIfNotExists.getCompanyPaymentTable());

    String companySql = "INSERT INTO company VALUES (?,?,?,?,?,?,?,?,?) RETURNING id ";
    String paymentTypeSql = "INSERT INTO companypayment VALUES (?,?,?,?)";
    long id = jdbcTemplate.queryForObject(companySql, companyObject(entry), Long.class);
    jdbcTemplate
        .update(paymentTypeSql, companyPaymentObject(entry.getPayments().iterator().next(), id));
    return id;
  }

  @Override
  public void deleteEntry(long id) {
    String deleteQuery = "DELETE FROM company WHERE id =?";
    String deleteCompanyPayment = "DELETE FROM companypayment WHERE id=?";
    jdbcTemplate.update(deleteQuery, id);
    jdbcTemplate.update(deleteCompanyPayment, id);
  }

  @Override
  @SuppressWarnings("unchecked")
  public Company getEntryById(long id) {
    String getPayment = "SELECT * FROM companypayment WHERE id =?";
    String getQuery = " SELECT * FROM company WHERE id =?";

    Payment payment = (Payment) jdbcTemplate.queryForObject(getPayment, new Object[]{id},
        new CompanyPaymentRowMapper());
    return (Company) jdbcTemplate
        .queryForObject(getQuery, new Object[]{id}, new CompanyRowMapper(payment));
  }

  @Override
  public void updateEntry(Company entry) {
    long id = entry.getId();
    String companyPaymenttSql = "UPDATE companypayment set issuedate=?, amount=?,"
        + "paymenttype=?, id=? WHERE id=";
    String insertSql = "UPDATE company set companyname = ?, issuedate = ?, address = ?, city = ?,"
        + " zipcode = ?, nip = ?, bankaccountnumber = ?, taxtype = ?, caruser = ? WHERE id=";
    jdbcTemplate.update(companyPaymenttSql + id,
        companyPaymentObject(entry.getPayments().iterator().next(), id));
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

  private void createCompanyTable() {
    jdbcTemplate.update("CREATE TABLE IF NOT EXISTS company "
        + "(companyname       varchar(256),"
        + "issuedate         date,"
        + "address           varchar(256),"
        + "city              varchar(256),"
        + "zipcode           varchar(256),"
        + "nip               varchar(256),"
        + "bankaccountnumber varchar(256),"
        + "taxtype           varchar(256),"
        + "caruser           boolean,"
        + "id               serial not null "
        + "constraint company_id_pk primary key);"
        + "create unique index if not exists company_id_uindex on company (id);");
  }
}