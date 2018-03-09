package pl.coderstrust.service;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import pl.coderstrust.model.HasNameIdIssueDate;
import pl.coderstrust.model.HasValidation;
import pl.coderstrust.model.Messages;

import java.time.LocalDate;
import java.util.List;

public abstract class BookController<T extends HasNameIdIssueDate & HasValidation> {

  protected Book<T> book;

  @RequestMapping(value = "", method = RequestMethod.POST)
  public ResponseEntity addEntry(@RequestBody T entry) {
    List<String> entryState = entry.validate();
    if (entryState.isEmpty()) {
      long id = book.addEntry(entry);
      return ResponseEntity.ok(Messages.CONTROLLER_INVOICE_ADDED + id);
    }
    return ResponseEntity.badRequest().body(entryState);
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  public ResponseEntity getEntryById(@PathVariable("id") long id) {
    if (!book.idExist(id)) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(book.findEntry(id));
  }

  @RequestMapping(value = "")
  public ResponseEntity getEntryByDate(
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
      @RequestParam(value = "startDate", required = false) LocalDate startDate,
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
      @RequestParam(value = "endDate", required = false) LocalDate endDate) {
    if (startDate == null && endDate == null) {
      return ResponseEntity.ok(book.getEntry());
    }
    return ResponseEntity.ok(book.getEntryByDate(startDate,
        endDate));
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
  public ResponseEntity updateInvoice(@PathVariable("id") long id, @RequestBody T entry) {
    List<String> entryState = entry.validate();
    if (!entryState.isEmpty()) {
      return ResponseEntity.badRequest().body(entryState);
    }
    entry.setId(id);
    book.updateEntry(entry);
    return ResponseEntity.ok().build();

  }

  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
  public ResponseEntity removeEntry(@PathVariable("id") long id) {
    if (!book.idExist(id)) {
      return ResponseEntity.notFound().build();
    }
    book.deleteEntry(id);
    return ResponseEntity.ok().build();
  }
}