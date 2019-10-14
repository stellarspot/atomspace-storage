package atomspace.storage.relationaldb;

import atomspace.storage.util.AtomspaceStorageHelper;

import java.sql.SQLException;

public class AtomspaceRelationalDBStorageHelper implements AtomspaceStorageHelper<ASRelationalDBTransaction> {


    private final AtomspaceRelationalDBStorage storage;

    public AtomspaceRelationalDBStorageHelper(AtomspaceRelationalDBStorage storage) {
        this.storage = storage;
    }

    @Override
    public void dump(ASRelationalDBTransaction tx) {
        try {
            tx.dump();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void reset(ASRelationalDBTransaction tx) {
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
