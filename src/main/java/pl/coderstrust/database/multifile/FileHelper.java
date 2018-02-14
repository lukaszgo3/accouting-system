package pl.coderstrust.database.multifile;

        import pl.coderstrust.model.Invoice;

        import java.io.*;
        import java.util.ArrayList;
        import java.util.List;

public class FileHelper {

    public void addLine(String lineContent, Invoice invoice) {
        PathSelector pathSelector;
        String dataPath = new PathSelector().getFilePath(invoice);
        lineContent += System.lineSeparator();
        File file = new File(dataPath);
        file.getParentFile().mkdirs();
        try {
            FileWriter fw = new FileWriter(file, true);
            fw.append(lineContent);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Adding invoice:" + invoice.getSystemId());
    }

    public ArrayList<String> getAllFilesEntries() throws IOException {
        List allFiles;
        String line = null;
        ArrayList readedFiles = new ArrayList();
        FileCashe fileCashe = new FileCashe();
        fileCashe.listf("database");
        allFiles = fileCashe.listf("database");
        for (int i = 0; i < allFiles.size(); i++) {
            String path = allFiles.get(i).toString();
            BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
            while ((line = bufferedReader.readLine()) != null) {
                readedFiles.add(line);
            }
        }

        return readedFiles;
    }
}