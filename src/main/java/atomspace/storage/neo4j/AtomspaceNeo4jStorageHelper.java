package atomspace.storage.neo4j;

import atomspace.storage.util.AtomspaceStorageHelper;

public class AtomspaceNeo4jStorageHelper implements AtomspaceStorageHelper {


    private final ASNeo4jTransaction tx;

    public AtomspaceNeo4jStorageHelper(ASNeo4jTransaction tx) {
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
