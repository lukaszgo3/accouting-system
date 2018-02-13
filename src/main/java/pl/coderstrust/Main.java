package pl.coderstrust;

import org.apache.tomcat.jni.Local;

import java.time.LocalDate;

public class Main {
  public static void main(String[] args) {
    LocalDate localDate = LocalDate.parse("2019-12-01");
    System.out.println(localDate);
  }
}
