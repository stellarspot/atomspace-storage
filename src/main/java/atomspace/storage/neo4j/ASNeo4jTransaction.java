package atomspace.storage.neo4j;

import atomspace.storage.ASAtom;
import atomspace.storage.ASLink;
import atomspace.storage.ASNode;
import atomspace.storage.ASTransaction;
import atomspace.storage.base.ASBaseLink;
import atomspace.storage.base.ASBaseNode;
import org.neo4j.graphdb.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static atomspace.storage.util.AtomspaceStorageUtils.count;
import static atomspace.storage.util.AtomspaceStorageUtils.getKey;

public class ASNeo4jTransaction implements ASTransaction {

    final GraphDatabaseService graph;
    private final Transaction tx;

    public ASNeo4jTransaction(GraphDatabaseService graph) {
        this.graph = graph;
        this.tx = graph.beginTx();
    }

    @Override
    public ASNode get(String type, String value) {

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

        return new ASBaseNode(node.getId(), type, value);
    }

    @Override
    public ASLink get(String type, ASAtom... atoms) {

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

            // Update incoming set
            int arity = atoms.length;
            for (int i = 0; i < arity; i++) {
                String key = getKey(type, arity, i);
                Node childNode = graph.getNodeById(atoms[i].getId());
                childNode.createRelationshipTo(node, RelationshipType.withName(key));
            }

        }

        return new ASBaseLink(node.getId(), type, atoms);
    }

    @Override
    public ASAtom get(long id) {

        Node node = graph.getNodeById(id);
        String kind = node.getProperty("kind").toString();
        String type = node.getProperty("type").toString();

        if ("Node".equals(kind)) {
            String value = node.getProperty("value").toString();
            return new ASBaseNode(id, type, value);
        }

        if ("Link".equals(kind)) {
            long[] ids = (long[]) node.getProperty("ids");
            return new ASBaseLink(id, type, ids);
        }

        String msg = String.format("Unknown kind: %s%n", kind);
        throw new RuntimeException(msg);
    }

    @Override
    public long[] getOutgoingListIds(long id) {
        Node node = graph.getNodeById(id);
        return (long[]) node.getProperty("ids");
    }

    @Override
    public int getIncomingSetSize(long id, String type, int arity, int position) {
        // TBD: use the count store
        return count(getIncomingRelationships(id, type, arity, position).iterator());
    }

    @Override
    public Iterator<ASLink> getIncomingSet(long id, String type, int arity, int position) {
        List<ASLink> links = new ArrayList<>();
        for (Relationship r : getIncomingRelationships(id, type, arity, position)) {
            Node parent = r.getEndNode();
            links.add(new ASBaseLink(parent.getId(), type, arity));
        }

        return links.iterator();
    }

    private Iterable<Relationship> getIncomingRelationships(long id, String type, int size, int position) {
        String key = getKey(type, size, position);
        Node node = graph.getNodeById(id);
        return node.getRelationships(RelationshipType.withName(key), Direction.OUTGOING);
    }


    @Override
    public Iterator<ASAtom> getAtoms() {
        List<ASAtom> atoms = new ArrayList<>();

        Iterator<Node> nodes = graph.findNodes(Label.label("Node"));

        while (nodes.hasNext()) {
            atoms.add(get(nodes.next().getId()));
        }

        Iterator<Node> links = graph.findNodes(Label.label("Link"));

        while (links.hasNext()) {
            atoms.add(get(links.next().getId()));
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
