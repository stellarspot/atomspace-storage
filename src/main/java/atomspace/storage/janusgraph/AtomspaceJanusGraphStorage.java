package atomspace.storage.janusgraph;

import atomspace.storage.AtomspaceStorage;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphFactory;

public class AtomspaceJanusGraphStorage implements AtomspaceStorage {

    final JanusGraph graph;

    public AtomspaceJanusGraphStorage(String storageDirectory) {
        graph = JanusGraphFactory.build()
                .set("storage.backend", "berkeleyje")
                .set("storage.directory", String.format("%s/graph", storageDirectory))
                .set("index.search.backend", "lucene")
                .set("index.search.directory", String.format("%s/index", storageDirectory))
//                .set("query.force-index", true)
                .open();
    }


    @Override
    public AtomspaceJanusGraphStorageTransaction getTx() {
        return new AtomspaceJanusGraphStorageTransaction(graph);
    }

    @Override
    public void close() throws Exception {
        graph.close();
    }
}
