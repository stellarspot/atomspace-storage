package atomspace.storage.performance;

import atomspace.storage.janusgraph.AtomspaceJanusGraphStorageTransaction;
import atomspace.storage.janusgraph.AtomspaceJanusGraphStorage;
import atomspace.storage.janusgraph.AtomspaceJanusGraphStorageHelper;
import atomspace.storage.memory.AtomspaceMemoryStorage;
import atomspace.storage.neo4j.AtomSpaceNeo4jStorage;
import atomspace.storage.neo4j.AtomspaceNeo4jStorageHelper;
import atomspace.storage.neo4j.AtomspaceNeo4jStorageTransaction;

public class AtomspaceStoragePerformanceUtils {

    private static final String STORAGE_DIR = "/tmp/atomspace-storage/perf";

    public static AtomspaceMemoryStorage getCleanMemoryStorage() {
        return new AtomspaceMemoryStorage();
    }

    public static AtomSpaceNeo4jStorage getCleanNeo4jStorage() {

        String storageDir = String.format("%s/neo4j", STORAGE_DIR);
        AtomSpaceNeo4jStorage storage = new AtomSpaceNeo4jStorage(storageDir);

        try (AtomspaceNeo4jStorageTransaction tx = storage.getTx()) {
            AtomspaceNeo4jStorageHelper helper = new AtomspaceNeo4jStorageHelper(tx);
            helper.reset();
        }

        return storage;
    }

    public static AtomspaceJanusGraphStorage getCleanJanusGraphStorage() {

        String storageDir = String.format("%s/janusgraph", STORAGE_DIR);
        AtomspaceJanusGraphStorage storage = new AtomspaceJanusGraphStorage(storageDir);

        try (AtomspaceJanusGraphStorageTransaction tx = storage.getTx()) {
            AtomspaceJanusGraphStorageHelper helper = new AtomspaceJanusGraphStorageHelper(tx);
            helper.reset();
        }

        return storage;
    }
}
