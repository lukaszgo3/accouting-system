package pl.coderstrust.service;

import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import pl.coderstrust.model.Messages;
import pl.coderstrust.model.WithNameIdIssueDate;
import pl.coderstrust.model.WithValidation;

import java.time.LocalDate;
import java.util.List;

public abstract class AbstractController<T extends WithNameIdIssueDate & WithValidation> {

  protected AbstractService<T> service;
  private final Logger logger = LoggerFactory.getLogger(AbstractController.class);

  @RequestMapping(value = "", method = RequestMethod.POST)
  @ApiOperation(value = "Adds the entries and returning id")
  public ResponseEntity addEntry(@RequestBody T entry) {
    List<String> entryState = entry.validate();
    if (entryState.isEmpty()) {
      long id = service.addEntry(entry);
      logger.info("Response adding from " + getClass().getSimpleName() + ": "
          + ResponseEntity.ok(Messages.CONTROLLER_ENTRY_ADDED + id));
      return ResponseEntity.ok(Messages.CONTROLLER_ENTRY_ADDED + id);
    }
    return ResponseEntity.badRequest().body(entryState);
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  @ApiOperation(value = "Returns the entry by id")
  public ResponseEntity getEntryById(@PathVariable("id") long id) {
    if (!service.idExist(id)) {
      logger.info("Response adding from " + getClass().getSimpleName() + ": "
          + ResponseEntity.noContent().build());
      return ResponseEntity.notFound().build();
    }
    logger.info("Response adding from " + getClass().getSimpleName() + "\n"
        + ResponseEntity.ok(service.findEntry(id)));
    return ResponseEntity.ok(service.findEntry(id));
  }

  @RequestMapping(value = "", method = RequestMethod.GET)
  @ApiOperation(value = "Returns the list of entries in the specified date range")
  public ResponseEntity getEntryByDate(
      @RequestParam(value = "startDate", required = false) LocalDate startDate,
      @RequestParam(value = "endDate", required = false) LocalDate endDate) {
    if (startDate == null && endDate == null) {
      logger.info("Response getting all data (no value) from: " + getClass().getSimpleName()
          + ResponseEntity.ok(service.getEntry()));
      return ResponseEntity.ok(service.getEntry());
    }
    logger.info("Response getting data in specific range from: " + getClass().getSimpleName()
        + ResponseEntity.ok(service.getEntryByDate(startDate, endDate)));
    return ResponseEntity.ok(service.getEntryByDate(startDate, endDate));
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
  @ApiOperation(value = "Updates the entries by id")
  public ResponseEntity updateInvoice(@PathVariable("id") long id, @RequestBody T entry) {
    List<String> entryState = entry.validate();
    if (!entryState.isEmpty()) {
      logger.info("Response update from: " + getClass().getSimpleName()
          + ResponseEntity.badRequest().body(entryState));
      return ResponseEntity.badRequest().body(entryState);
    }
    entry.setId(id);
    service.updateEntry(entry);
    logger.info("Response update from: " + getClass().getSimpleName()
        + ResponseEntity.ok().build());
    return ResponseEntity.ok().build();
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
  @ApiOperation(value = "Deletes the entries by id")
  public ResponseEntity removeEntry(@PathVariable("id") long id) {
    if (!service.idExist(id)) {
      logger.info("Response delete (not found) from: " + getClass().getSimpleName()
          + ResponseEntity.notFound().build());
      return ResponseEntity.notFound().build();
    }
    service.deleteEntry(id);
    logger.info("Response delete from: " + getClass().getSimpleName()
        + ResponseEntity.ok().build());
    return ResponseEntity.ok().build();
  }
}