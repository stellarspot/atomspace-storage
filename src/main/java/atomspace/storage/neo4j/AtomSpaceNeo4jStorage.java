package atomspace.storage.neo4j;

import atomspace.storage.AtomspaceStorage;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.schema.IndexDefinition;
import org.neo4j.graphdb.schema.Schema;

import java.io.File;

public class AtomSpaceNeo4jStorage implements AtomspaceStorage {

    final GraphDatabaseService graph;

    public AtomSpaceNeo4jStorage(String storageDirectory) {
        this.graph = new GraphDatabaseFactory().newEmbeddedDatabase(new File(storageDirectory));
        makeIndices();
    }

    @Override
    public AtomspaceNeo4jStorageTransaction getTx() {
        return new AtomspaceNeo4jStorageTransaction(graph);
    }

    @Override
    public void close() {
        graph.shutdown();
    }

    private void makeIndices() {
        try (Transaction tx = graph.beginTx()) {

            graph
                    .schema()
                    .indexFor(Label.label("Node"))
                    .on("type")
                    .on("value")
                    .create();

            graph
                    .schema()
                    .indexFor(Label.label("Link"))
                    .on("type")
                    .on("ids")
                    .create();

            tx.success();
        }
    }
}
