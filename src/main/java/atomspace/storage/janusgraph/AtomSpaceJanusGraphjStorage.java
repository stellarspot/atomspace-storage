package atomspace.storage.janusgraph;

import atomspace.storage.AtomspaceStorage;
import atomspace.storage.AtomspaceStorageTransaction;
import atomspace.storage.neo4j.AtomSpaceNeo4jStorageTransaction;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphFactory;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.File;

public class AtomSpaceJanusGraphjStorage implements AtomspaceStorage {

    final JanusGraph graph;

    public AtomSpaceJanusGraphjStorage(String storageDirectory) {
        graph = JanusGraphFactory.build()
                .set("storage.backend", "berkeleyje")
                .set("storage.directory", String.format("%s/graph", storageDirectory))
                .set("index.search.backend", "lucene")
                .set("index.search.directory", String.format("%s/index", storageDirectory))
//                .set("query.force-index", true)
                .open();

    }


    @Override
    public AtomspaceStorageTransaction getTx() {
        return new AtomSpaceJanusGraphStorageTransaction(graph);
    }

    @Override
    public void close() throws Exception {
        graph.close();
    }
}
