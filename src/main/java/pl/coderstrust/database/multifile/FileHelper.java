package pl.coderstrust.database.multifile;


        import org.apache.commons.io.FileUtils;
        import pl.coderstrust.model.Invoice;

        import java.io.*;
        import java.nio.file.Files;
        import java.nio.file.Paths;
        import java.nio.file.StandardCopyOption;
        import java.util.ArrayList;
        import java.util.List;
        import java.util.regex.Matcher;

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

    public String getLine (long id) throws IOException {

        FileCache fileCache = new FileCache();
        String path = fileCache.cashe.get(id).toString();
        System.out.println(fileCache.cashe.get(id));

        String foundLine = null;
        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
        String line = null;
            while ((line = bufferedReader.readLine())!=null){
                if (line.contains("systemId\":"+id)){
                    foundLine=line;
                }
            }
            bufferedReader.close();
            return foundLine;
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


    public void deleteLine (long id) throws IOException {

        FileCache fileCache = new FileCache();
        File inputFile = new File(fileCache.cashe.get(id).toString());
        File tempFile = new File(fileCache.cashe.get(id).toString()+"temp");

        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

        String lineToRemove = "systemId\":"+id;
        String currentLine;

        while((currentLine = reader.readLine()) != null) {
            String trimmedLine = currentLine.trim();
            if(trimmedLine.contains(lineToRemove)) continue;
            writer.write(currentLine+=System.lineSeparator());
        }
        writer.close();
        reader.close();
        boolean successful = tempFile.renameTo(inputFile);
        System.out.println(successful);
    }

    public List<File> listFiles(String directoryName) {
        File dir = new File(directoryName);
        String[] extensions = new String[]{"json"};
        List<File> files = (List<File>) FileUtils.listFiles(dir, extensions, true);
        return files;
    }
}