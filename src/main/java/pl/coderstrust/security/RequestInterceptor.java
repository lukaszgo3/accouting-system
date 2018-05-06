package pl.coderstrust.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import pl.coderstrust.database.file.InFileDatabase;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class RequestInterceptor extends HandlerInterceptorAdapter {


  private final Logger logger = LoggerFactory.getLogger("Security");

  /**
   * This is not a good practice to use sysout. Always integrate any logger with your application.
   * We will discuss about integrating logger with spring boot application in some later article
   */
  @Override
  public boolean preHandle(HttpServletRequest request,
      HttpServletResponse response, Object object) throws Exception {

    if (request.getPathInfo().equals("/tokens/validate")) {
      return true;
    }
    System.out.println(request.getPathInfo() + " @@@@@@@@@@@@@@@@@@@@@@@@");
    String requestToken = request.getHeader("Token");
    if (requestToken == null || requestToken.isEmpty()) {
      return false;
    }

    URL url = new URL("http://localhost:8080/tokens/validate?token=" + requestToken);
    HttpURLConnection con = (HttpURLConnection) url.openConnection();
    con.setRequestMethod("GET");

//    String params = "?token=" + requestToken;
//    con.setDoOutput(true);
//
//    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(con.getOutputStream());
//    outputStreamWriter.write(params);
//    outputStreamWriter.flush();

//    System.out.println("%%%%%%%%%%%%%%%%%" + con.getOutputStream());

    con.setConnectTimeout(5000);
    con.setReadTimeout(5000);

    BufferedReader in = new BufferedReader(
        new InputStreamReader(con.getInputStream()));
    String inputLine;
    StringBuffer content = new StringBuffer();
    while ((inputLine = in.readLine()) != null) {
      content.append(inputLine);
    }
    in.close();

    con.disconnect();

    if (content.toString().equals("true")) {
      return true;
    } else {
      logger.error("Your token is BAD BAD BAD");
      return false;
    }


  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response,
      Object object, ModelAndView model)
      throws Exception {
    System.out.println("_________________________________________");
    System.out.println("In postHandle request processing "
        + "completed by @RestController");
    System.out.println("_________________________________________");
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
      Object object, Exception arg3)
      throws Exception {
    System.out.println("________________________________________");
    System.out.println("In afterCompletion Request Completed");
    System.out.println("________________________________________");
  }
}
