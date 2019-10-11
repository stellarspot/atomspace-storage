package atomspace.storage.janusgraph;

import atomspace.storage.ASAtom;
import atomspace.storage.ASLink;
import atomspace.storage.ASTransaction;
import atomspace.storage.base.ASBaseLink;
import atomspace.storage.base.ASBaseNode;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphVertex;
import org.janusgraph.core.Transaction;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static atomspace.storage.util.AtomspaceStorageUtils.count;
import static atomspace.storage.util.AtomspaceStorageUtils.getKey;

public class ASJanusGraphTransaction implements ASTransaction {

    final AtomspaceJanusGraphStorage storage;
    final JanusGraph graph;
    final Transaction tx;
    final GraphTraversalSource g;

    public ASJanusGraphTransaction(AtomspaceJanusGraphStorage storage) {
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

        return new ASBaseNode(id(vertex), type, value);
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

            // Update incoming set
            int arity = atoms.length;
            for (int i = 0; i < arity; i++) {
                String key = getKey(type, arity, i);
                Vertex childVertex = g.V(atoms[i].getId()).next();
                childVertex.addEdge(key, vertex);
            }
        }

        return new ASBaseLink(id(vertex), type, atoms);
    }

    @Override
    public ASAtom get(long id) {
        Vertex vertex = vertex(id);
        String kind = vertex.property("kind").value().toString();
        String type = vertex.property("type").value().toString();

        if ("Node".equals(kind)) {
            String value = vertex.property("value").value().toString();
            return new ASBaseNode(id, type, value);
        }

        if ("Link".equals(kind)) {
            long[] ids = (long[]) vertex.property("ids").value();
            return new ASBaseLink(id, type, ids);
        }

        String msg = String.format("Unknown kind: %s%n", kind);
        throw new RuntimeException(msg);
    }

    @Override
    public long[] getOutgoingListIds(long id) {
        return (long[]) vertex(id).property("ids").value();
    }

    @Override
    public int getIncomingSetSize(long id, String type, int arity, int position) {
        // TBD: use the count store
        return count(getSet(id, type, arity, position));
    }

    @Override
    public Iterator<ASLink> getIncomingSet(long id, String type, int arity, int position) {
        List<ASLink> links = new ArrayList<>();
        Iterator<Edge> iter = getSet(id, type, arity, position);
        while (iter.hasNext()) {
            JanusGraphVertex parent = (JanusGraphVertex) iter.next().inVertex();
            links.add(new ASBaseLink(id(parent), type, arity));
        }

        return links.iterator();
    }

    private Iterator<Edge> getSet(long id, String type, int size, int position) {
        Vertex vertex = g.V(id).next();
        String key = getKey(type, size, position);
        return vertex.edges(Direction.OUT, key);
    }

    @Override
    public Iterator<ASAtom> getAtoms() {

        List<ASAtom> atoms = new ArrayList<>();

        GraphTraversal<Vertex, Vertex> nodes = g.V().has("kind", "Node");

        while (nodes.hasNext()) {
            ASAtom node = get(id(nodes.next()));
            atoms.add(node);
        }

        GraphTraversal<Vertex, Vertex> links = g.V().has("kind", "Link");

        while (links.hasNext()) {
            ASAtom link = get(id(links.next()));
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

    private static long id(Vertex v) {
        return (long) v.id();
    }

    private Vertex vertex(long id) {
        return g.V(id).next();
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
