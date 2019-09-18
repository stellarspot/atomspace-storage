package atomspace.storage.janusgraph;

import atomspace.storage.AtomspaceStorage;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphFactory;
import org.janusgraph.core.schema.JanusGraphIndex;
import org.janusgraph.core.schema.JanusGraphManagement;

public class AtomspaceJanusGraphStorage implements AtomspaceStorage {

    final JanusGraph graph;

    public AtomspaceJanusGraphStorage(String storageDirectory) {
        graph = JanusGraphFactory.build()
                .set("storage.backend", "berkeleyje")
                .set("storage.directory", String.format("%s/graph", storageDirectory))
                .set("index.search.backend", "lucene")
                .set("index.search.directory", String.format("%s/index", storageDirectory))
                //.set("query.force-index", true)
                .open();

        makeIndices();
    }


    @Override
    public AtomspaceJanusGraphStorageTransaction getTx() {
        return new AtomspaceJanusGraphStorageTransaction(graph);
    }

    @Override
    public void close() throws Exception {
        graph.close();
    }


    private void makeIndices() {
        JanusGraphManagement mgmt = graph.openManagement();

        createIndex(mgmt, "kindIndex", "kind");
        createIndexWithLabel(mgmt, "nodeIndex", "Node", "type", "value");
        createIndexWithLabel(mgmt, "linkIndex", "Link", "type", "ids");

        for (JanusGraphIndex ind : mgmt.getGraphIndexes(Vertex.class)) {
            System.out.printf("vertex index: %s%n", ind);
        }

        mgmt.commit();
    }

    private static void createIndex(JanusGraphManagement mgmt, String indexName, String... keys) {
        createIndexWithLabel(mgmt, indexName, "", keys);
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
}
