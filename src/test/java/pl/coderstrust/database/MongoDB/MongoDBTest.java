package pl.coderstrust.database.MongoDB;

import pl.coderstrust.database.Database;
import pl.coderstrust.database.DatabaseTest;
import pl.coderstrust.database.mongo.MongoDatabase;
import pl.coderstrust.model.Invoice;

public class MongoDBTest extends DatabaseTest {

  @Override
  public Database getCleanDatabase() {
    return new MongoDatabase(Invoice.class, null, true);
  }

  @Override
  public void shouldDeleteSingleInvoiceById() throws Exception {
  }
}
