package atomspace.storage.relationaldb;

import atomspace.storage.util.AtomspaceStorageHelper;

import java.sql.SQLException;

public class ASRelationalDBTestUtils {

    public static final String DB_URL_JUNIT = "jdbc:derby:/tmp/atomspace-storage/junit/relationaldb;create=true";
    private static final AtomSpaceRelationalDBStorage RELATIONALDB_STORAGE_STORAGE;

    static {
        try {
            RELATIONALDB_STORAGE_STORAGE = new AtomSpaceRelationalDBStorage(DB_URL_JUNIT);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static AtomSpaceRelationalDBStorage getTestStorage() {
        resetStorage();
        return RELATIONALDB_STORAGE_STORAGE;
    }

    private static void resetStorage() {
        try (AtomspaceRelationalDBStorageTransaction tx = RELATIONALDB_STORAGE_STORAGE.getTx()) {
            AtomspaceStorageHelper helper = new AtomspaceRelationalDBStorageHelper(tx);
            helper.reset();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
