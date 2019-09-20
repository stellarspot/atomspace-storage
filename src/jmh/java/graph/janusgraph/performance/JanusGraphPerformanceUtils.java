package graph.janusgraph.performance;

import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphFactory;
import org.janusgraph.core.JanusGraphTransaction;
import org.janusgraph.core.schema.JanusGraphIndex;
import org.janusgraph.core.schema.JanusGraphManagement;

public class JanusGraphPerformanceUtils {

    private static final String STORAGE_DIR = "/tmp/atomspace-storage/other/perf";

    public static JanusGraph getJanusGraph() {

        String storageDirectory = String.format("%s/janusgraph", STORAGE_DIR);

        JanusGraph graph = JanusGraphFactory.build()
                .set("storage.backend", "berkeleyje")
                //.set("storage.backend", "inmemory")
                .set("storage.directory", String.format("%s/graph", storageDirectory))
                .set("index.search.backend", "lucene")
                .set("index.search.directory", String.format("%s/index", storageDirectory))
                //.set("query.force-index", true)
                .open();

        resetJanusGraph(graph);
        makeIndices(graph);

        return graph;
    }

    private static void makeIndices(JanusGraph graph) {

        JanusGraphManagement mgmt = graph.openManagement();
        createIndexWithLabel(mgmt, "testIndex", getLabel(), getKey());

        for (JanusGraphIndex index : mgmt.getGraphIndexes(Vertex.class)) {
            System.out.printf("vertex index: %s%n", index.name());
        }

        mgmt.commit();
    }

    private static void createIndexWithLabel(JanusGraphManagement mgmt, String indexName, String label, String... keys) {

        if (mgmt.getGraphIndex(indexName) == null) {
            JanusGraphManagement.IndexBuilder builder = mgmt.buildIndex(indexName, Vertex.class);

            for (String key : keys) {
                builder = builder.addKey(mgmt.getOrCreatePropertyKey(key));
            }

            if (!label.isEmpty()) {
                builder.indexOnly(mgmt.getOrCreateVertexLabel(label));
            }

            builder.buildCompositeIndex();
        }
    }

    public static long getVerticesCount(JanusGraph graph) {
        try (JanusGraphTransaction tx = graph.newTransaction()) {
            return tx.traversal().V().count().next();
        }
    }

    private static void resetJanusGraph(JanusGraph graph) {

        try (JanusGraphTransaction tx = graph.newTransaction()) {
            tx.traversal().E().drop().iterate();
            tx.traversal().V().drop().iterate();
            tx.commit();
        }
    }

    public static void assertVerticesCount(JanusGraph graph, String msg, int count) {
        long vertices = JanusGraphPerformanceUtils.getVerticesCount(graph);
        System.out.printf("%s: %d", msg, vertices);

        if (vertices != count) {
            String err = String.format("expected vertices: %d. actual: %d", count, vertices);
            throw new RuntimeException(err);
        }
    }

    public static String getLabel() {
        return "TestLabel";
    }

    public static String getKey() {
        return "TestKey";
    }

    public static String getValue(int i) {
        return String.format("value_%d", i);
    }
}
