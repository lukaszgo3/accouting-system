package pl.coderstrust.database;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.coderstrust.database.memory.InMemoryDatabase;
import pl.coderstrust.model.Company;
import pl.coderstrust.model.Invoice;


@Configuration
public class DatabaseProvider {

  @Bean
  public Database<Invoice> withInvoice() {
    return new InMemoryDatabase<>(Invoice.class);
  }

  @Bean
  public Database<Company> withCompany() {
    return new InMemoryDatabase<>(Company.class);
  }
}
