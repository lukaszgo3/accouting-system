package pl.coderstrust.database.multifile;

import pl.coderstrust.database.Database;

public class MiltiFileDatabaseTestAll extends DatabaseTest {
    @Override
    public Database getDatabase() {
        return new MultiFileDatabase();
    }
}
