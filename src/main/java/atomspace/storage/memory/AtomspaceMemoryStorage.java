package atomspace.storage.memory;

import atomspace.storage.*;

public class AtomspaceMemoryStorage implements AtomspaceStorage {

    private final AtomspaceMemoryStorageTransaction tx = new AtomspaceMemoryStorageTransaction();

    @Override
    public AtomspaceStorageTransaction getTx() {
        return tx;
    }

    @Override
    public void close() {
    }
}
