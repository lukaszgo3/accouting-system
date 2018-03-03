package pl.coderstrust.database.multifile;

import java.io.File;

public class Configuration {

  private static final String jsonFilePath =
      "src" + File.separator + "main" + File.separator + "resources" + File.separator;
  private static final String jsonTempFilePath =
      "src" + File.separator + "main" + File.separator + "resources" + File.separator
          + "temporary.json";

  public static String getJsonFilePath() {
    return jsonFilePath;
  }

  public static String getJsonTempFilePath() {
    return jsonTempFilePath;
  }
}