package atomspace.layer.gremlin;

import atomspace.layer.gremlin.AtomspaceGremlinStorage.TransactionWithSource;
import atomspace.storage.janusgraph.AtomspaceJanusGraphStorageHelper;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.janusgraph.core.JanusGraph;

public class AtomspaceGremlinStorageHelper {

    public static AtomspaceGremlinStorage getInMemoryJanusGraph() {
        JanusGraph graph = AtomspaceJanusGraphStorageHelper.getInMemoryJanusGraph(false);
        GremlinJanusGraphStorage storage = new GremlinJanusGraphStorage(graph);
        return new AtomspaceGremlinStorage(storage);
    }

    static class GremlinJanusGraphStorage implements AtomspaceGremlinStorage.Storage {

        private final JanusGraph graph;

        public GremlinJanusGraphStorage(JanusGraph graph) {
            this.graph = graph;
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
    }
}
