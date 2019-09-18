package atomspace.storage.janusgraph;

import atomspace.ASTestUtils;
import atomspace.storage.memory.AtomspaceMemoryStorageHelper;
import atomspace.storage.memory.AtomspaceMemoryStorageTransaction;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

public class ASJanusGraphTestUtils {

    public static final String JANUSGRAPH_STORAGE_DIR = "/tmp/atomspace-storage/junit/janusgraph";

    public static AtomSpaceJanusGraphjStorage getTestStorage() {
        ASTestUtils.removeDirectory(JANUSGRAPH_STORAGE_DIR);
        return new AtomSpaceJanusGraphjStorage(JANUSGRAPH_STORAGE_DIR);
    }

    public static AtomspaceJanusGraphStorageHelper getStorageHelper(AtomSpaceJanusGraphStorageTransaction tx) {
        return new AtomspaceJanusGraphStorageHelper(tx);
    }

}
