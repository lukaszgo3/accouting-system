package pl.coderstrust.database.multifile;

import pl.coderstrust.model.Invoice;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileHelper {



    public void addLine(String lineContent, Invoice invoice) {
        PathSelector pathSelector;
        String dataPath = new PathSelector().getFilePath(invoice);
        lineContent += System.lineSeparator();
        File file = new File(dataPath);
        file.getParentFile().mkdirs();
        try {
        FileWriter fw = new FileWriter(file,true);
        fw.append(lineContent);
        fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Adding invoice:" + invoice.getSystemId());
        }
    }
