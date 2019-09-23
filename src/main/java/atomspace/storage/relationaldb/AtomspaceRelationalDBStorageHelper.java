package atomspace.storage.relationaldb;

import atomspace.storage.util.AtomspaceStorageHelper;

import java.sql.SQLException;

public class AtomspaceRelationalDBStorageHelper implements AtomspaceStorageHelper {


    private final AtomspaceRelationalDBStorageTransaction tx;

    public AtomspaceRelationalDBStorageHelper(AtomspaceRelationalDBStorageTransaction tx) {
        this.tx = tx;
    }

    @Override
    public void dump() {
    }

    @Override
    public void reset() {
        try {
            tx.reset();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
