package atomspace.storage.neo4j;

import atomspace.storage.ASAtom;
import atomspace.storage.ASIncomingSet;
import atomspace.storage.AtomspaceStorageTransaction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

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
            node.setProperty("kind", "Node");
            node.setProperty("type", type);
            node.setProperty("value", value);
        }

        return new ASNeo4jNode(node);
    }

    @Override
    public ASAtom get(String type, ASAtom... atoms) {

        Label linkLabel = Label.label(type);
        long[] ids = getIds(atoms);
        Node node = graph.findNode(linkLabel, "ids", ids);

        if (node == null) {

            node = graph.createNode();
            node.addLabel(linkLabel);
            node.setProperty("kind", "Link");
            node.setProperty("type", type);
            node.setProperty("ids", ids);

            ASNeo4jLink link = new ASNeo4jLink(node);
            int size = atoms.length;
            for (int i = 0; i < size; i++) {
                atoms[i].getIncomingSet().add(link, size, i);
            }

            return link;
        }

        return new ASNeo4jLink(node);
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


    private static long[] getIds(ASAtom... atoms) {
        long[] ids = new long[atoms.length];

        for (int i = 0; i < atoms.length; i++) {
            ids[i] = atoms[i].getId();
        }
        return ids;
    }
}
