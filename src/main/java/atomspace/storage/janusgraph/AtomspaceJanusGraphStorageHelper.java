package atomspace.storage.janusgraph;

import atomspace.storage.util.AtomspaceStorageHelper;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphFactory;

import java.util.Iterator;

public class AtomspaceJanusGraphStorageHelper implements AtomspaceStorageHelper<ASJanusGraphTransaction> {


    private final AtomspaceJanusGraphStorage storage;

    public AtomspaceJanusGraphStorageHelper(AtomspaceJanusGraphStorage storage) {
        this.storage = storage;
    }

    @Override
    public void dump(ASJanusGraphTransaction tx) {
        tx.dump();
    }

    @Override
    public void reset(ASJanusGraphTransaction tx) {
        tx.reset();
    }

    @Override
    public void printStatistics(ASJanusGraphTransaction tx, String msg) {
        tx.printStatistics(msg);
    }

    public static AtomspaceJanusGraphStorage getJanusGraphInMemoryStorage() {
        JanusGraph graph = JanusGraphFactory.build()
                .set("storage.backend", "inmemory")
                .set("graph.set-vertex-id", "true")
                .set("ids.block-size", "10000000")
                .set("ids.authority.wait-time", "5")
                //.set("ids.renew-timeout", "50")
                //.set("query.force-index", true)
                .open();
        return new AtomspaceJanusGraphStorage(graph);
    }

    public static AtomspaceJanusGraphStorage getJanusGraphBerkeleyDBStorage(String storageDirectory) {
        JanusGraph graph = JanusGraphFactory.build()
                .set("storage.backend", "berkeleyje")
                .set("storage.directory", String.format("%s/graph", storageDirectory))
                .set("index.search.backend", "lucene")
                .set("index.search.directory", String.format("%s/index", storageDirectory))
                .set("graph.set-vertex-id", "true")
                //.set("ids.block-size", "100000")
                //.set("ids.authority.wait-time", "15")
                //.set("ids.renew-timeout", "50")
                //.set("query.force-index", true)
                .open();
        return new AtomspaceJanusGraphStorage(graph);
    }
}
