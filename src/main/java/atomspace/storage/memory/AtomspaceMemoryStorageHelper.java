package atomspace.storage.memory;

import atomspace.storage.util.AtomspaceStorageHelper;

public class AtomspaceMemoryStorageHelper implements AtomspaceStorageHelper<ASMemoryTransaction> {


    private final AtomspaceMemoryStorage storage;

    public AtomspaceMemoryStorageHelper(AtomspaceMemoryStorage storage) {
        this.storage = storage;
    }

    @Override
    public void dump(ASMemoryTransaction tx) {
    }

    @Override
    public void reset(ASMemoryTransaction tx) {
        tx.reset();
    }
}
