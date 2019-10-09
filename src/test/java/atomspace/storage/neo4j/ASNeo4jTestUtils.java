package atomspace.storage.neo4j;

import atomspace.ASTestUtils;
import atomspace.storage.util.AtomspaceStorageHelper;

public class ASNeo4jTestUtils {

    public static final String NEO4J_STORAGE_DIR = "/tmp/atomspace-storage/junit/neo4j";
    private static final AtomspaceNeo4jStorage NEO4J_STORAGE;

    static {
        ASTestUtils.removeDirectory(NEO4J_STORAGE_DIR);
        NEO4J_STORAGE = new AtomspaceNeo4jStorage(NEO4J_STORAGE_DIR);
        addShutdownHook();
    }

    public static AtomspaceNeo4jStorage getTestStorage() {
        resetStorage();
        return NEO4J_STORAGE;
    }

    public static AtomspaceNeo4jStorageHelper getStorageHelper(ASNeo4jTransaction tx) {
        return new AtomspaceNeo4jStorageHelper(tx);
    }

    private static void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            NEO4J_STORAGE.close();
        }));
    }

    private static void resetStorage() {
        ASNeo4jTransaction tx = NEO4J_STORAGE.getTx();
        AtomspaceStorageHelper helper = new AtomspaceNeo4jStorageHelper(tx);
        helper.reset();
    }
}
