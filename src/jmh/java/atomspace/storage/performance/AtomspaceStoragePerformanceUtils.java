package atomspace.storage.performance;

import atomspace.storage.AtomspaceStorage;
import atomspace.storage.memory.AtomspaceMemoryStorage;
import atomspace.storage.neo4j.AtomSpaceNeo4jStorage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

public class AtomspaceStoragePerformanceUtils {

    private static final String STORAGE_DIR = "/tmp/atomspace-storage/perf";

    public static AtomspaceMemoryStorage getCleanMemoryStorage() {
        return new AtomspaceMemoryStorage();
    }

    public static AtomSpaceNeo4jStorage getCleanNeo4jStorage() {

        String storageDir = String.format("%s/neo4j", STORAGE_DIR);
        removeDir(storageDir);
        return new AtomSpaceNeo4jStorage(storageDir);
    }

    private static void removeDir(String dir) {
        Path pathToBeDeleted = Paths.get(dir);

        if (!Files.exists(pathToBeDeleted)) {
            return;
        }

        try {
            Files.walk(pathToBeDeleted)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
