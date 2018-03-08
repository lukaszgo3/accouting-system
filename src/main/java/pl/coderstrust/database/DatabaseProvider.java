package pl.coderstrust.database;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.coderstrust.database.file.singleFile.InFileDatabase;
import pl.coderstrust.model.Invoice;


@Configuration
public class DatabaseProvider {
  @Bean
  public Database<Invoice> withInvoice() {
    return new InFileDatabase<>(Invoice.class);
  }

//  @Bean
//  public Database<Company> withCompany() {
//    return new InFileDatabase<>(Company.class);
//  }
}
