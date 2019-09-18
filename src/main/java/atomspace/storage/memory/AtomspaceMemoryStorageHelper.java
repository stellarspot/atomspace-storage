package atomspace.storage.memory;

import atomspace.storage.util.AtomspaceStorageHelper;

public class AtomspaceMemoryStorageHelper implements AtomspaceStorageHelper {


    private final AtomspaceMemoryStorageTransaction tx;

    public AtomspaceMemoryStorageHelper(AtomspaceMemoryStorageTransaction tx) {
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
