package pl.coderstrust.database.multifile;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderstrust.database.DbException;
import pl.coderstrust.database.ExceptionMsg;
import pl.coderstrust.model.Invoice;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FileHelper {

  private File tempFile;
  private FileCache fileCache;
  private PathSelector pathSelector;

  @Autowired
  public FileHelper(FileCache fileCache, PathSelector pathSelector) {
    tempFile = new File(Configuration.getJsonTempFilePath());
    this.fileCache = fileCache;
    this.pathSelector = pathSelector;
  }

  private void initializeDatabaseFile() {
    if (!tempFile.exists()) {
      try {
        tempFile.createNewFile();
      } catch (IOException e) {
        throw new DbException(
            ExceptionMsg.IO_ERROR_WHILE_INITIALIZING, e);
        //TODO add logging.
      }
    }
  }

  public void addLine(String lineContent, Invoice invoice) {
    try {
      String dataPath = pathSelector.getFilePath(invoice);
      lineContent += System.lineSeparator();
      File file = new File(dataPath);
      file.getParentFile().mkdirs();
      FileWriter fw = new FileWriter(file, true);
      fw.append(lineContent);
      fw.close();
    } catch (IOException e) {
      throw new DbException(
          ExceptionMsg.IO_ERROR_WHILE_ADDING, e);
    }
    System.out.println("Adding invoice:" + invoice.getId());
  }

  public String getLine(long id) {
    String pathFile = fileCache.getCache().get(id).toString();
    String json = null;
    try {
      Stream<String> stream = Files.lines(new File(pathFile).toPath());
      json = stream.filter(line -> line.contains("id\":" + id))
          .collect(Collectors.joining());
    } catch (IOException e) {
      e.printStackTrace();
    }
    return json;
  }

  public void deleteLine(long id) {
    File inputFile = new File(fileCache.getCache().get(id).toString());
    File tempFile = new File(Configuration.getJsonTempFilePath());
    try {
      BufferedReader reader = new BufferedReader(new FileReader(inputFile));
      BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

      String lineToRemove = "id\":" + id;
      String currentLine;

      while ((currentLine = reader.readLine()) != null) {
        String trimmedLine = currentLine.trim();
        if (trimmedLine.contains(lineToRemove)) {
          continue;
        }
        writer.write(currentLine += System.lineSeparator());
      }
      writer.close();
      reader.close();

      RandomAccessFile rafInput = new RandomAccessFile(inputFile, "rw");
      rafInput.close();
      inputFile.delete();

      RandomAccessFile rafTemp = new RandomAccessFile(tempFile, "rw");
      rafTemp.close();
      tempFile.renameTo(inputFile);
      tempFile.delete();
    } catch (IOException e) {
      throw new DbException(ExceptionMsg.IO_ERROR_WHILE_DELETING, e);
      //TODO add logging.
    }
  }

}