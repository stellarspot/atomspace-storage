package atomspace.storage.neo4j;

import atomspace.storage.AtomspaceStorage;
import atomspace.storage.AtomspaceStorageTransaction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.File;

public class AtomSpaceNeo4jStorage implements AtomspaceStorage {

    final GraphDatabaseService graph;

    public AtomSpaceNeo4jStorage(String storageDirectory) {
        this.graph = new GraphDatabaseFactory().newEmbeddedDatabase(new File(storageDirectory));
    }

    @Override
    public AtomspaceStorageTransaction getTx() {
        return new AtomSpaceNeo4jStorageTransaction(graph);
    }

    @Override
    public void close() throws Exception {
        graph.shutdown();
    }
}
