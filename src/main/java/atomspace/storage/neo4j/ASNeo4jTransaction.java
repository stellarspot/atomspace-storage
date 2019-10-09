package atomspace.storage.neo4j;

import atomspace.storage.ASAtom;
import atomspace.storage.ASLink;
import atomspace.storage.ASTransaction;
import org.neo4j.graphdb.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ASNeo4jTransaction implements ASTransaction {

    final GraphDatabaseService graph;
    private final Transaction tx;

    public ASNeo4jTransaction(GraphDatabaseService graph) {
        this.graph = graph;
        this.tx = graph.beginTx();
    }

    @Override
    public ASAtom get(String type, String value) {

        Label nodeLabel = Label.label("Node");
        Iterator<Node> nodes = graph.findNodes(nodeLabel,
                "type", type,
                "value", value);

        Node node = null;

        if (nodes.hasNext()) {
            node = nodes.next();
        }

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

        Label linkLabel = Label.label("Link");
        long[] ids = getIds(atoms);
        Iterator<Node> nodes = graph.findNodes(linkLabel,
                "type", type,
                "ids", ids);

        Node node = null;

        if (nodes.hasNext()) {
            node = nodes.next();
        }

        if (node == null) {

            node = graph.createNode();
            node.addLabel(linkLabel);
            node.setProperty("kind", "Link");
            node.setProperty("type", type);
            node.setProperty("ids", ids);

            ASNeo4jLink link = new ASNeo4jLink(node);
            int size = atoms.length;
            for (int i = 0; i < size; i++) {
                ((ASNeo4jAtom.ASNeo4jIncomingSet) atoms[i].getIncomingSet()).add(link, size, i);
            }

            return link;
        }

        return new ASNeo4jLink(node);
    }

    @Override
    public ASAtom get(long id) {
        throw new UnsupportedOperationException("Get atom by id.");
    }

    @Override
    public long[] getOutgoingListIds(long id) {
        throw new UnsupportedOperationException("Get ids by id.");
    }

    @Override
    public int getIncomingSetSize(long id, String type, int arity, int position) {
        throw new UnsupportedOperationException("Get incoming set arity by id.");
    }

    @Override
    public Iterator<ASLink> getIncomingSet(long id, String type, int arity, int position) {
        throw new UnsupportedOperationException("Get incoming set by id.");
    }

    @Override
    public Iterator<ASAtom> getAtoms() {
        List<ASAtom> atoms = new ArrayList<>();

        Iterator<Node> nodes = graph.findNodes(Label.label("Node"));

        while (nodes.hasNext()) {
            atoms.add(new ASNeo4jNode(nodes.next()));
        }

        Iterator<Node> links = graph.findNodes(Label.label("Link"));

        while (links.hasNext()) {
            atoms.add(new ASNeo4jLink(links.next()));
        }

        return atoms.iterator();
    }

    @Override
    public void commit() {
        tx.success();
    }

    @Override
    public void close() {
        tx.close();
    }

    void reset() {

        for (Relationship r : graph.getAllRelationships()) {
            r.delete();
        }

        for (Node n : graph.getAllNodes()) {
            n.delete();
        }
    }

    private static long[] getIds(ASAtom... atoms) {
        long[] ids = new long[atoms.length];

        for (int i = 0; i < atoms.length; i++) {
            ids[i] = atoms[i].getId();
        }
        return ids;
    }
}
