package atomspace.storage.janusgraph;

import atomspace.storage.AtomspaceStorage;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.schema.JanusGraphManagement;
import org.janusgraph.graphdb.database.StandardJanusGraph;
import org.janusgraph.graphdb.idmanagement.IDManager;

import java.util.concurrent.atomic.AtomicLong;

public class AtomspaceJanusGraphStorage implements AtomspaceStorage {

    final JanusGraph graph;
    final IDManager idManager;
    final AtomicLong currentId = new AtomicLong();

    public AtomspaceJanusGraphStorage(JanusGraph graph) {
        this.graph = graph;
        this.idManager = ((StandardJanusGraph) graph).getIDManager();
        makeIndices();
    }

    @Override
    public ASJanusGraphTransaction getTx() {
        return new ASJanusGraphTransaction(this);
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

    long getNextId() {
        return idManager.toVertexId(currentId.incrementAndGet());
    }
}
