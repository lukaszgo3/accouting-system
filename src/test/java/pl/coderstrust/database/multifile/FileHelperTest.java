package pl.coderstrust.database.multifile;

import org.junit.Test;

import java.io.IOException;

public class FileHelperTest {

    @Test
    public void addLine() {
    }

    @Test
    public void getAllFilesEntries() {

        FileHelper fileHelper = new FileHelper();
        try {
            fileHelper.getAllFilesEntries();
            System.out.println(fileHelper.getAllFilesEntries().get(5));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}