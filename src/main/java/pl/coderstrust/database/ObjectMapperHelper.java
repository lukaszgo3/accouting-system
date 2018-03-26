package pl.coderstrust.database;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ObjectMapperHelper<T> {

  private final Logger logger = LoggerFactory.getLogger(ObjectMapperHelper.class);
  private ObjectMapper jsonMapper;
  private Class<T> entryClass;

  public ObjectMapperHelper(Class<T> entryClass) {
    jsonMapper = new ObjectMapper();
    jsonMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    jsonMapper.registerModule(new JavaTimeModule());
    this.entryClass = entryClass;
  }

  public String toJson(T object) {
    try {
      return jsonMapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      logger.warn(" from toJson: "
          + ExceptionMsg.INTERNAL_PROCESSING_ERROR, e);
      throw new DbException(ExceptionMsg.INTERNAL_PROCESSING_ERROR, e);
    }
  }

  public T toObject(String json) {
    try {
      return jsonMapper.readValue(json, entryClass);
    } catch (IOException e) {
      logger.warn(" from toObject: "
          + ExceptionMsg.INTERNAL_PROCESSING_ERROR, e);
      throw new DbException(ExceptionMsg.INTERNAL_PROCESSING_ERROR, e);
    }
  }
}
