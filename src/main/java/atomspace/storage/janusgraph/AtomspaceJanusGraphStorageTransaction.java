package atomspace.storage.janusgraph;

import atomspace.storage.ASAtom;
import atomspace.storage.ASLink;
import atomspace.storage.ASNode;
import atomspace.storage.AtomspaceStorageTransaction;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.Transaction;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AtomspaceJanusGraphStorageTransaction implements AtomspaceStorageTransaction {

    final AtomspaceJanusGraphStorage storage;
    final JanusGraph graph;
    private final Transaction tx;
    private final GraphTraversalSource g;

    public AtomspaceJanusGraphStorageTransaction(AtomspaceJanusGraphStorage storage) {
        this.storage = storage;
        this.graph = storage.graph;
        this.tx = graph.newTransaction();
        this.g = tx.traversal();
    }

    @Override
    public ASAtom get(String type, String value) {

        GraphTraversal<Vertex, Vertex> iter = g
                .V()
                .hasLabel("Node")
                .has("type", type)
                .has("value", value);

        Vertex vertex = null;
        if (iter.hasNext()) {
            vertex = iter.next();
        }

        if (vertex == null) {
            vertex = g
                    .addV("Node")
                    .property(T.id, storage.getNextId())
                    .property("kind", "Node")
                    .property("type", type)
                    .property("value", value).next();
        }

        return new ASJanusGraphNode(vertex);
    }

    @Override
    public ASAtom get(String type, ASAtom... atoms) {

        long[] ids = getIds(atoms);

        GraphTraversal<Vertex, Vertex> iter = g
                .V()
                .hasLabel("Link")
                .has("type", type)
                .has("ids", ids);

        Vertex vertex = null;
        if (iter.hasNext()) {
            vertex = iter.next();
        }

        if (vertex == null) {
            vertex = g
                    .addV("Link")
                    .property(T.id, storage.getNextId())
                    .property("kind", "Link")
                    .property("type", type)
                    .property("ids", ids).next();

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

        GraphTraversal<Vertex, Vertex> nodes = g.V().has("kind", "Node");

        while (nodes.hasNext()) {
            ASNode node = new ASJanusGraphNode(nodes.next());
            atoms.add(node);
        }

        GraphTraversal<Vertex, Vertex> links = g.V().has("kind", "Link");

        while (links.hasNext()) {
            ASLink link = new ASJanusGraphLink(links.next());
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
        g.E().drop().iterate();
        g.V().drop().iterate();
    }
}
