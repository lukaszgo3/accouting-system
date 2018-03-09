package pl.coderstrust.service;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.coderstrust.model.Company;

@RestController
@RequestMapping("company")
@Configuration
public class CompanyBookController extends BookController<Company> {

  public CompanyBookController(CompanyBook companyBook) {
    super.book = companyBook;
  }
}
