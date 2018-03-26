package pl.coderstrust.service;


import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.coderstrust.model.Company;

import java.time.LocalDate;

@RestController
@RequestMapping("company")
public class CompanyController extends AbstractController<Company> {

  public CompanyController(CompanyService companyService, CompanyDummyFilter dummyFilter) {
    super.service = companyService;
    super.byCustomerFilter = dummyFilter;
  }

  @RequestMapping(value = "v2/company", method = RequestMethod.POST)
  @ApiOperation(value = "Adds the company and returning id")
  public ResponseEntity addCompany(
      @RequestBody Company company) {
    return super.addEntry(company, null);
  }

  @RequestMapping(value = "v2/company/{id}", method = RequestMethod.GET)
  @ApiOperation(value = "Returns the company by id")
  public ResponseEntity getCompany(
      @PathVariable("id") Long companyId) {
    return super.getEntryById(companyId, null);
  }

  @RequestMapping(value = "v2/company", method = RequestMethod.GET)
  @ApiOperation(value = "Returns the list of companies in the specified date range")
  public ResponseEntity getCompanyByDate(
      @RequestParam(name = "startDate", required = false) LocalDate startDate,
      @RequestParam(name = "endDate", required = false) LocalDate endDate) {
    return super.getEntryByDate(startDate, endDate, null);
  }

  @RequestMapping(value = "/v2/company/{id}", method = RequestMethod.PUT)
  @ApiOperation(value = "Updates the company by id")
  public ResponseEntity updateCompany(
      @PathVariable("id") Long id,
      @RequestBody Company company) {
    return super.updateEntry(id, company, null);
  }

  @RequestMapping(value = {"/v2/company/{id}"}, method = RequestMethod.DELETE)
  @ApiOperation(value = "Deletes the company by id")
  public ResponseEntity removeCompany(
      @PathVariable("id") Long id) {
    return removeEntry(id, null);
  }
}