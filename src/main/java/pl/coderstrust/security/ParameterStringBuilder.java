package pl.coderstrust.security;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

public class ParameterStringBuilder {

  public static String getParamsString(Map<String, String> params)
      throws UnsupportedEncodingException {
    StringBuilder result = new StringBuilder();

    for (Map.Entry<String, String> entry : params.entrySet()) {
      result.append(entry.getKey());
      result.append("=");
      result.append(entry.getValue());
    }

    String resultString = result.toString();

    System.out.println("$$$$$$$$$$$$$$$$$$$$$$$" + resultString);

    return resultString.length() > 0
        ? resultString.substring(0, resultString.length() - 1)
        : resultString;
  }
}