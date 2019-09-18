package atomspace.storage.neo4j;

import atomspace.ASTestUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

public class ASNeo4jTestUtils {

    public static final String NEO4J_STORAGE_DIR = "/tmp/atomspace-storage/junit/neo4j";

    public static AtomSpaceNeo4jStorage getTestStorage() {
        ASTestUtils.removeDirectory(NEO4J_STORAGE_DIR);
        return new AtomSpaceNeo4jStorage(NEO4J_STORAGE_DIR);
    }
}
