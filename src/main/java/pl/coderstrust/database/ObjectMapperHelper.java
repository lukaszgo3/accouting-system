package pl.coderstrust.database;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;

//@Service
public class ObjectMapperHelper<T> {

  private ObjectMapper jsonMapper;
  private Class entryClass;

  public ObjectMapperHelper(Class entryClass) {
    jsonMapper = new ObjectMapper();
    jsonMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    jsonMapper.registerModule(new JavaTimeModule());
    this.entryClass = entryClass;
  }

  public String toJson(T object) {
    try {
      return jsonMapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      throw new DbException(ExceptionMsg.INTERNAL_PROCESSING_ERROR, e);
      //TODO add logging.
    }
  }

  public  T toObject(String json) {
    try {
     return  (T) jsonMapper.readValue(json, entryClass);
    } catch (IOException e) {
      throw new DbException(ExceptionMsg.INTERNAL_PROCESSING_ERROR, e);
      //TODO add logging.
    }
  }
}
