package atomspace.storage.janusgraph;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

public class ASJanusGraphTestUtils {

    public static final String JANUSGRAPH_STORAGE_DIR = "/tmp/atomspace-storage/junit/janusgraph";

    public static AtomSpaceJanusGraphjStorage getTestStorage() {
        removeTestStorage();
        return new AtomSpaceJanusGraphjStorage(JANUSGRAPH_STORAGE_DIR);
    }

    private static void removeTestStorage() {
        Path pathToBeDeleted = Paths.get(JANUSGRAPH_STORAGE_DIR);

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
