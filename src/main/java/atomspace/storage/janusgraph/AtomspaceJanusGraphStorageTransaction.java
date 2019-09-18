package atomspace.storage.janusgraph;

import atomspace.storage.ASAtom;
import atomspace.storage.ASLink;
import atomspace.storage.ASNode;
import atomspace.storage.AtomspaceStorageTransaction;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphVertex;
import org.janusgraph.core.Transaction;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AtomspaceJanusGraphStorageTransaction implements AtomspaceStorageTransaction {

    final JanusGraph graph;
    private final Transaction tx;

    public AtomspaceJanusGraphStorageTransaction(JanusGraph graph) {
        this.graph = graph;
        this.tx = graph.newTransaction();
    }

    @Override
    public ASAtom get(String type, String value) {

        GraphTraversal<Vertex, Vertex> iter = tx.traversal().V()
                .hasLabel("Node")
                .has("type", type)
                .has("value", value);

        JanusGraphVertex vertex = null;
        if (iter.hasNext()) {
            vertex = (JanusGraphVertex) iter.next();
        }

        if (vertex == null) {
            vertex = tx.addVertex("Node");
            vertex.property("kind", "Node");
            vertex.property("type", type);
            vertex.property("value", value);
        }

        return new ASJanusGraphNode(vertex);
    }

    @Override
    public ASAtom get(String type, ASAtom... atoms) {

        long[] ids = getIds(atoms);

        GraphTraversal<Vertex, Vertex> iter = tx.traversal().V()
                .hasLabel("Link")
                .has("type", type)
                .has("ids", ids);

        JanusGraphVertex vertex = null;
        if (iter.hasNext()) {
            vertex = (JanusGraphVertex) iter.next();
        }

        if (vertex == null) {
            vertex = tx.addVertex("Link");
            vertex.property("kind", "Link");
            vertex.property("type", type);
            vertex.property("ids", ids);

            ASJanusGraphLink link = new ASJanusGraphLink(vertex);
            int size = atoms.length;
            for (int i = 0; i < size; i++) {
                atoms[i].getIncomingSet().add(link, size, i);
            }

            return link;
        }

        return new ASJanusGraphNode(vertex);
    }

    @Override
    public Iterator<ASAtom> getAtoms() {

        List<ASAtom> atoms = new ArrayList<>();

        GraphTraversal<Vertex, Vertex> nodes = tx.traversal().V().has("kind", "Node");

        while (nodes.hasNext()) {
            ASNode node = new ASJanusGraphNode((JanusGraphVertex) nodes.next());
            atoms.add(node);
        }

        GraphTraversal<Vertex, Vertex> links = tx.traversal().V().has("kind", "Link");

        while (links.hasNext()) {
            ASLink link = new ASJanusGraphLink((JanusGraphVertex) links.next());
            atoms.add(link);
        }

        return atoms.iterator();
    }

    @Override
    public void commit() {
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

    void reset() {
        tx.traversal().E().drop().iterate();
        tx.traversal().V().drop().iterate();
    }
}
