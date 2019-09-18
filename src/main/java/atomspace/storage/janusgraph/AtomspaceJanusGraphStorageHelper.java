package atomspace.storage.janusgraph;

import atomspace.storage.memory.AtomspaceMemoryStorageTransaction;
import atomspace.storage.util.AtomspaceStorageHelper;

public class AtomspaceJanusGraphStorageHelper implements AtomspaceStorageHelper {


    private final AtomSpaceJanusGraphStorageTransaction tx;

    public AtomspaceJanusGraphStorageHelper(AtomSpaceJanusGraphStorageTransaction tx) {
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
