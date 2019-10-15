package atomspace.performance.runner;

import atomspace.storage.AtomspaceStorage;

public interface StorageWrapper {

    String getName();

    AtomspaceStorage getStorage();

    void clean();

    void printStatistics();

    default void close() throws Exception {
        getStorage().close();
    }
}
