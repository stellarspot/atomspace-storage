package atomspace.storage.neo4j;

import atomspace.storage.util.AtomspaceStorageHelper;

public class AtomspaceNeo4jStorageHelper implements AtomspaceStorageHelper {


    private final AtomspaceNeo4jStorageTransaction tx;

    public AtomspaceNeo4jStorageHelper(AtomspaceNeo4jStorageTransaction tx) {
        this.tx = tx;
    }

    @Override
    public void dump() {
    }

    @Override
    public void reset() {
        tx.reset();
    }
}
