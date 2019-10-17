package atomspace.layer.gremlin;

import atomspace.layer.gremlin.AtomspaceGremlinStorage.TransactionWithSource;
import atomspace.storage.ASTransaction;
import atomspace.storage.janusgraph.AtomspaceJanusGraphStorageHelper;
import atomspace.storage.janusgraph.JanusGraphUtils;
import atomspace.storage.util.AtomspaceStorageHelper;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.graphdb.database.StandardJanusGraph;
import org.janusgraph.graphdb.idmanagement.IDManager;

import java.util.concurrent.atomic.AtomicLong;

public class AtomspaceGremlinStorageHelper implements AtomspaceStorageHelper {

    @Override
    public void dump(ASTransaction tx) {
        ((ASGremlinTransaction) tx).dump();
    }

    @Override
    public void reset(ASTransaction tx) {
        ((ASGremlinTransaction) tx).reset();
    }

    @Override
    public void printStatistics(ASTransaction tx, String msg) {
        ((ASGremlinTransaction) tx).printStatistics(msg);
    }

    public static AtomspaceGremlinStorage getInMemoryJanusGraph() {
        JanusGraph graph = AtomspaceJanusGraphStorageHelper.getInMemoryJanusGraph(true);
        GremlinJanusGraphStorage storage = new GremlinJanusGraphStorage(graph);
        return new AtomspaceGremlinStorage(storage);
    }

    static class GremlinJanusGraphStorage implements AtomspaceGremlinStorage.Storage {

        private final JanusGraph graph;
        private final IDManager idManager;
        private final AtomicLong currentId = new AtomicLong();

        public GremlinJanusGraphStorage(JanusGraph graph) {
            this.graph = graph;
            this.idManager = ((StandardJanusGraph) graph).getIDManager();
        }

        @Override
        public TransactionWithSource get() {
            GraphTraversalSource g = graph.newTransaction().traversal();
            return new TransactionWithSource(g.tx(), g);
        }

        @Override
        public void close() {
            graph.close();
        }

        @Override
        public void makeIndices() {
            JanusGraphUtils.makeIndices(graph);
        }

        @Override
        public long getNextId() {
            return JanusGraphUtils.getNextId(idManager, currentId);
        }
    }
}
