package pl.coderstrust.database.sql;

class CreateSqlTablesIfNotExists {

  String getCompanyTable() {
    return "CREATE TABLE IF NOT EXISTS company "
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
        + "create unique index if not exists company_id_uindex on company (id);";
  }

  String getCompanyPaymentTable() {
    return "CREATE TABLE IF NOT EXISTS companypayment"
        + "(issuedate   date,"
        + "amount      numeric,"
        + "paymenttype varchar(256),"
        + "id          integer);";
  }
}
