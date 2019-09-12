package atomspace.storage.neo4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

public class ASNeo4jTestUtils {

    public static final String NEO4J_STORAGE_DIR = "/tmp/atomspace-storage/junit/neo4j";

    public static AtomSpaceNeo4jStorage getTestStorage() {
        removeTestStorage();
        return new AtomSpaceNeo4jStorage(NEO4J_STORAGE_DIR);
    }

    private static void removeTestStorage() {
        Path pathToBeDeleted = Paths.get(NEO4J_STORAGE_DIR);

        if(!Files.exists(pathToBeDeleted)) {
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
