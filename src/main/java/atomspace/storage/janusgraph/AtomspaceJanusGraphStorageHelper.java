package atomspace.storage.janusgraph;

import atomspace.storage.util.AtomspaceStorageHelper;

public class AtomspaceJanusGraphStorageHelper implements AtomspaceStorageHelper {


    private final AtomspaceJanusGraphStorageTransaction tx;

    public AtomspaceJanusGraphStorageHelper(AtomspaceJanusGraphStorageTransaction tx) {
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
