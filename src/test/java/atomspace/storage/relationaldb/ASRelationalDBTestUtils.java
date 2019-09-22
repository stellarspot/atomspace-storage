package atomspace.storage.relationaldb;

import java.sql.SQLException;

public class ASRelationalDBTestUtils {

    public static final String DB_URL_JUNIT = "jdbc:derby:ATOMSPACE_JUNIT;create=true";
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
    }
}
