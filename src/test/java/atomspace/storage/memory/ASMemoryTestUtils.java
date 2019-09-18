package atomspace.storage.memory;

import atomspace.storage.AtomspaceStorage;

public class ASMemoryTestUtils {

    public static AtomspaceMemoryStorage getTestStorage() {
        return new AtomspaceMemoryStorage();
    }

    public static AtomspaceMemoryStorageHelper getStorageHelper(AtomspaceMemoryStorageTransaction tx) {
        return new AtomspaceMemoryStorageHelper(tx);
    }
}