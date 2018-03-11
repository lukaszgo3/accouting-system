package pl.coderstrust.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pl.coderstrust.database.Database;
import pl.coderstrust.model.Company;

@Service
public class CompanyBook extends Book<Company> {

  @Autowired
  public CompanyBook(@Qualifier("withCompanies") Database<Company> dbCompanies) {
    super.database = dbCompanies;
  }
}

