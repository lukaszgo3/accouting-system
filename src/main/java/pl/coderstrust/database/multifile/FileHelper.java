package pl.coderstrust.database.multifile;

import pl.coderstrust.model.Invoice;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileHelper {



    public void addLine(String lineContent, Invoice invoice) {
        PathSelector pathSelector;
        String dataPath = new PathSelector().getFilePath(invoice);
        lineContent += System.lineSeparator();
        try {
            Files.write(Paths.get(dataPath), lineContent.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
        FileWriter fw = new FileWriter(dataPath,true);
        fw.append(lineContent);
        fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Adding invoice:" + invoice.getSystemId());
        }
    }
