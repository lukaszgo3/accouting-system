package pl.coderstrust.database.sql;

import org.springframework.jdbc.core.RowMapper;
import pl.coderstrust.model.Payment;
import pl.coderstrust.model.PaymentType;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CompanyPaymentRowMapper implements RowMapper {

  @Override
  public Payment mapRow(ResultSet resultSet, int i) throws SQLException {
    Payment payment = new Payment();
    payment.setId(resultSet.getInt("id"));
    payment.setIssueDate(resultSet.getDate("issuedate").toLocalDate());
    payment.setAmount(resultSet.getBigDecimal("amount"));
    payment.setType(PaymentType.valueOf(resultSet.getString("paymenttype")));
    return payment;
  }
}