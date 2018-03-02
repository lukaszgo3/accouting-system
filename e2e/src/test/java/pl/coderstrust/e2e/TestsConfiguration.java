package pl.coderstrust.e2e;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.mapper.factory.Jackson2ObjectMapperFactory;

import java.math.BigDecimal;

public class TestsConfiguration {

  private final static String BASE_PATH = "/invoice/";
  private final static String BASE_URI = "http://localhost";
  private final static int BASE_PORT = 8080;
  private final static int TEST_INVOICES_COUNT = 10;
  private final static int DEFAULT_ENTRIES_COUNT = 5;
  private final static int SERVER_OK_STATUS_CODE = 200;
  private final static String INT_FROM_STRING_REGEX_PATTERN = "([0-9])+";
  private final static int DEFAULT_TEST_INVOICE_NUMBER = 1;
  private final static int DEFAULT_PRODUCT_QUANTITY = 1;
  private final static BigDecimal WRONG_NET_VALUE = new BigDecimal(-1);

  public TestsConfiguration() {
    RestAssured.baseURI = getBaseUri();
    RestAssured.basePath = getBasePath();

    String port = System.getProperty("server.port");
    if (port == null) {
      RestAssured.port = getBasePort();
    } else {
      RestAssured.port = Integer.valueOf(port);
    }

    RestAssured.config = RestAssuredConfig.config().objectMapperConfig(
        ObjectMapperConfig.objectMapperConfig().jackson2ObjectMapperFactory(new Jackson2ObjectMapperFactory() {
          @Override
          public ObjectMapper create(Class cls, String charset) {
            ObjectMapper jsonMapper = new ObjectMapper();
            jsonMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            jsonMapper.registerModule(new JavaTimeModule());
            return jsonMapper;
          }
        })
    );
  }

  public String getBasePath() {
    return BASE_PATH;
  }

  public String getBaseUri() {
    return BASE_URI;
  }

  public int getBasePort() {
    return BASE_PORT;
  }

  public int getTestInvoicesCount() {
    return TEST_INVOICES_COUNT;
  }

  public int getDefaultEntriesCount() {
    return DEFAULT_ENTRIES_COUNT;
  }

  public int getServerOkStatusCode() {
    return SERVER_OK_STATUS_CODE;
  }

  public String getIntFromStringRegexPattern() {
    return INT_FROM_STRING_REGEX_PATTERN;
  }

  public int getDefaultTestInvoiceNumber() {
    return DEFAULT_TEST_INVOICE_NUMBER;
  }

  public int getDefaultProductQuantity() {
    return DEFAULT_PRODUCT_QUANTITY;
  }

  public BigDecimal getWrongNetValue() {
    return WRONG_NET_VALUE;
  }
}
