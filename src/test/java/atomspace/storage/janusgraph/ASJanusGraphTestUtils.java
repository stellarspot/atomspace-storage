package atomspace.storage.janusgraph;

import atomspace.ASTestUtils;
import atomspace.storage.util.AtomspaceStorageHelper;

public class ASJanusGraphTestUtils {

    private static final String JANUSGRAPH_STORAGE_DIR = "/tmp/atomspace-storage/junit/janusgraph";
    private static final AtomSpaceJanusGraphjStorage JANUS_GRAPHJ_STORAGE;

    static {
        ASTestUtils.removeDirectory(JANUSGRAPH_STORAGE_DIR);
        JANUS_GRAPHJ_STORAGE = new AtomSpaceJanusGraphjStorage(JANUSGRAPH_STORAGE_DIR);
        addShutdownHook();
    }

    public static AtomSpaceJanusGraphjStorage getTestStorage() {
        resetStorage();
        return JANUS_GRAPHJ_STORAGE;
    }

    public static AtomspaceJanusGraphStorageHelper getStorageHelper(AtomSpaceJanusGraphStorageTransaction tx) {
        return new AtomspaceJanusGraphStorageHelper(tx);
    }

    private static void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                JANUS_GRAPHJ_STORAGE.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }));
    }

    private static void resetStorage() {
        try (AtomSpaceJanusGraphStorageTransaction tx = JANUS_GRAPHJ_STORAGE.getTx()) {
            AtomspaceStorageHelper helper = new AtomspaceJanusGraphStorageHelper(tx);
            helper.reset();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}