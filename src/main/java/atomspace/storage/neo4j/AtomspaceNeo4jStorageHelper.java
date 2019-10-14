package atomspace.storage.neo4j;

import atomspace.storage.util.AtomspaceStorageHelper;

public class AtomspaceNeo4jStorageHelper implements AtomspaceStorageHelper<ASNeo4jTransaction> {

    private final AtomspaceNeo4jStorage storage;

    public AtomspaceNeo4jStorageHelper(AtomspaceNeo4jStorage storage) {
        this.storage = storage;
    }

    @Override
    public void dump(ASNeo4jTransaction tx) {
    }

    @Override
    public void reset(ASNeo4jTransaction tx) {
        tx.reset();
    }
}
