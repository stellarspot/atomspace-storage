package atomspace.storage.relationaldb;

import atomspace.storage.util.AtomspaceStorageHelper;

import java.sql.SQLException;

public class AtomspaceRelationalDBStorageHelper implements AtomspaceStorageHelper {


    private final ASRelationalDBTransaction tx;

    public AtomspaceRelationalDBStorageHelper(ASRelationalDBTransaction tx) {
        this.tx = tx;
    }

    @Override
    public void dump() {
        try {
            tx.dump();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void reset() {
        try {
            tx.reset();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static AtomspaceRelationalDBStorage getInMemoryStorage(String directory) {
        String url = String.format("jdbc:derby:%s;create=true;", directory);
        try {
            return new AtomspaceRelationalDBStorage(url);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
