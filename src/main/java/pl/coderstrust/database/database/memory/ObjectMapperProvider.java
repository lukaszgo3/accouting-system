package pl.coderstrust.database.database.memory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import pl.coderstrust.model.Invoice;

import java.io.IOException;

public class ObjectMapperProvider {

  private ObjectMapper objectMapper;

  /**
   * Construct object mapper and sets its proper configuration.
   */
  public ObjectMapperProvider() {
    objectMapper = new ObjectMapper();
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    objectMapper.registerModule(new JavaTimeModule());
  }

  /**
   * Wraps wireValueAsString method.
   *
   * @param value Object ot be converted to Json.
   * @return Json String representing Object.
   */

  public String toJson(Object value) throws JsonProcessingException {
    return objectMapper.writeValueAsString(value);
  }

  public Invoice toInvoice(String json) throws IOException {
    return objectMapper.readValue(json, Invoice.class);
  }
}
