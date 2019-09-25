package atomspace.storage.janusgraph;

import atomspace.storage.util.AtomspaceStorageHelper;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphFactory;

public class AtomspaceJanusGraphStorageHelper implements AtomspaceStorageHelper {


    private final AtomspaceJanusGraphStorageTransaction tx;

    public AtomspaceJanusGraphStorageHelper(AtomspaceJanusGraphStorageTransaction tx) {
        this.tx = tx;
    }

    @Override
    public void dump() {
    }

    @Override
    public void reset() {
        tx.reset();
    }

    @Override
    public void printStatistics(String msg) {
        long vertices = tx.g.V().count().next();
        long edges = tx.g.E().count().next();
        System.out.printf("%s vertices: %s, edges: %s%n", msg, vertices, edges);
    }

    public static AtomspaceJanusGraphStorage getJanusGraphInMemoryStorage() {
        JanusGraph graph = JanusGraphFactory.build()
                .set("storage.backend", "inmemory")
                .set("graph.set-vertex-id", "true")
                //.set("query.force-index", true)
                .open();
        return new AtomspaceJanusGraphStorage(graph);
    }

    public static AtomspaceJanusGraphStorage getJanusGraphBerkeleyDBStorage(String storageDirectory) {
        JanusGraph graph = JanusGraphFactory.build()
                .set("storage.backend", "berkeleyje")
                .set("storage.directory", String.format("%s/graph", storageDirectory))
                .set("graph.set-vertex-id", "true")
                .set("index.search.backend", "lucene")
                .set("index.search.directory", String.format("%s/index", storageDirectory))
                //.set("query.force-index", true)
                .open();
        return new AtomspaceJanusGraphStorage(graph);
    }
}
