package pl.coderstrust.database.multifile;

        import org.apache.commons.io.FileUtils;
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

        listFiles("database");
        allFiles = listFiles("database");
        for (int i = 0; i < allFiles.size(); i++) {
            String path = allFiles.get(i).toString();
            BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
            while ((line = bufferedReader.readLine()) != null) {
                readedFiles.add(line);
            }
        }

        return readedFiles;


    }
    public List<File> listFiles(String directoryName) {
        File dir = new File(directoryName);
        String[] extensions = new String[]{"json"};
        List<File> files = (List<File>) FileUtils.listFiles(dir, extensions, true);
        for (File file : files) {
            try {
                System.out.println("file: " + file.getCanonicalPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return files;
    }
}