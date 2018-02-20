package pl.coderstrust.database;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;

public class DbException extends RuntimeException {

  public DbException(String message) {
    super(message);
  }

  public DbException(String message, IOException previousException) {
    super(message);
  }

  public DbException(String message, InterruptedException previousException) {
    super(message);
  }

  public DbException(String message, JsonProcessingException previousException) {
    super(message);
  }

}
