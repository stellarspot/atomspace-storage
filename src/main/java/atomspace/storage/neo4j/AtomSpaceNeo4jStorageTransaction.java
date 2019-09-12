package atomspace.storage.neo4j;

import atomspace.storage.ASAtom;
import atomspace.storage.AtomspaceStorageTransaction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

import java.io.IOException;
import java.util.Iterator;

public class AtomSpaceNeo4jStorageTransaction implements AtomspaceStorageTransaction {

    final GraphDatabaseService graph;
    private final Transaction tx;

    public AtomSpaceNeo4jStorageTransaction(GraphDatabaseService graph) {
        this.graph = graph;
        this.tx = graph.beginTx();
    }

    @Override
    public ASAtom get(String type, String value) {

        Label nodeLabel = Label.label(type);
        Node node = graph.findNode(nodeLabel, "value", value);

        if (node == null) {
            node = graph.createNode();
            node.addLabel(nodeLabel);
            node.setProperty("value", value);
        }

        return new ASNeo4jNode(node);
    }

    @Override
    public ASAtom get(String type, ASAtom... atoms) {
        return null;
    }

    @Override
    public Iterator<ASAtom> getAtoms() {
        return null;
    }

    @Override
    public void commit() {
        tx.success();
    }

    @Override
    public void close() {
        tx.close();
    }
}
