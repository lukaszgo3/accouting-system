package pl.coderstrust.database.multifile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.coderstrust.database.DbException;
import pl.coderstrust.database.ExceptionMsg;
import pl.coderstrust.model.WithNameIdIssueDate;

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

public class FileHelper {

  private final Logger logger = LoggerFactory.getLogger(FileHelper.class);
  private FileCache fileCache;
  private PathSelector pathSelector;
  private String jsonTempFilePath;

  public FileHelper(FileCache fileCache, PathSelector pathSelector, String jsonTempFilePath) {
    this.fileCache = fileCache;
    this.pathSelector = pathSelector;
    this.jsonTempFilePath = jsonTempFilePath;
  }

  public void addLine(String lineContent, WithNameIdIssueDate invoice) {
    String dataPath = pathSelector.getFilePath(invoice);
    lineContent += System.lineSeparator();
    File file = new File(dataPath);
    file.getParentFile().mkdirs();
    try (FileWriter fw = new FileWriter(file, true)) {
      fw.append(lineContent);
      fw.close();
    } catch (IOException e) {
      logger.warn("WARNING from addLine in FileHelper (MultiFile): "
          + new DbException(ExceptionMsg.IO_ERROR_WHILE_ADDING, e));
      throw new DbException(ExceptionMsg.IO_ERROR_WHILE_ADDING, e);
    }
  }

  public String getLine(long id) {
    String pathFile = fileCache.getCache().get(id).toString();
    String json = null;
    try (Stream<String> stream = Files.lines(new File(pathFile).toPath())) {
      json = stream.filter(line -> line.contains("id\":" + id))
          .collect(Collectors.joining());
    } catch (IOException e) {
      logger.warn("WARNING from getLine in FileHelper (MultiFile): "
          + new DbException(ExceptionMsg.IO_ERROR_WHILE_READING, e));
      throw new DbException(ExceptionMsg.IO_ERROR_WHILE_READING, e);
    }
    return json;
  }

  public void deleteLine(long id) {
    File inputFile = new File(fileCache.getCache().get(id).toString());
    File tempFile = new File(jsonTempFilePath);
    try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
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
      logger.warn("WARNING from deleteLine in FileHelper (MultiFile): "
          + new DbException(ExceptionMsg.IO_ERROR_WHILE_DELETING, e));
      throw new DbException(ExceptionMsg.IO_ERROR_WHILE_DELETING, e);
    }
  }
}