package atomspace.layer.gremlin;

import atomspace.storage.ASAtom;
import atomspace.storage.ASLink;
import atomspace.storage.ASNode;
import atomspace.storage.ASTransaction;
import atomspace.storage.base.ASBaseLink;
import atomspace.storage.base.ASBaseNode;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Transaction;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import java.io.IOException;
import java.util.Iterator;

import static atomspace.storage.util.AtomspaceStorageUtils.getKey;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.addV;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.unfold;

public class ASGremlinTransaction implements ASTransaction {

    // "type" is a reserved property name in JanusGraph
    static final String KIND = "as_kind";
    static final String TYPE = "as_type";
    static final String VALUE = "as_value";
    static final String IDS = "as_ids";

    static final String LABEL_NODE = "Node";
    static final String LABEL_LINK = "Link";

    final Transaction tx;
    final GraphTraversalSource g;

    public ASGremlinTransaction(AtomspaceGremlinStorage.Storage storage) {
        AtomspaceGremlinStorage.TransactionWithSource pair = storage.get();
        this.tx = pair.tx();
        this.g = pair.traversal();
    }

    @Override
    public ASNode get(String type, String value) {
        Vertex v = g
                .V()
                .hasLabel(LABEL_NODE)
                .has(TYPE, type)
                .has(VALUE, value)
                .fold()
                .coalesce(
                        unfold(),
                        addV(LABEL_NODE)
                                .property(KIND, LABEL_NODE)
                                .property(TYPE, type)
                                .property(VALUE, value))
                .next();
        return new ASBaseNode(id(v), type, value);
    }

    @Override
    public ASLink get(String type, ASAtom... atoms) {
        long[] ids = getIds(atoms);

        GraphTraversal<Object, Vertex> addVertex = addV(LABEL_LINK)
                .property(KIND, LABEL_LINK)
                .property(TYPE, type)
                .property(IDS, ids);

        int arity = atoms.length;
        for (int i = 0; i < arity; i++) {
            String key = getKey(type, arity, i);
            long id = atoms[i].getId();
            addVertex = addVertex.addE(key).to(g.V(id)).outV();
        }

        Vertex v = g
                .V()
                .hasLabel(LABEL_LINK)
                .has(TYPE, type)
                .has(IDS, ids)
                .fold()
                .coalesce(unfold(), addVertex)
                .next();

        return new ASBaseLink(id(v), type, atoms);
    }

    @Override
    public ASAtom get(long id) {
        return null;
    }

    @Override
    public long[] getOutgoingListIds(long id) {
        return new long[0];
    }

    @Override
    public int getIncomingSetSize(long id, String type, int arity, int position) {
        return 0;
    }

    @Override
    public Iterator<ASLink> getIncomingSet(long id, String type, int arity, int position) {
        return null;
    }

    @Override
    public Iterator<ASAtom> getAtoms() {
        return null;
    }

    @Override
    public void commit() {
        tx.commit();
    }

    @Override
    public void close() throws IOException {
        tx.close();
    }

    private static long id(Vertex v) {
        return (long) v.id();
    }

    private static long[] getIds(ASAtom... atoms) {
        long[] ids = new long[atoms.length];

        for (int i = 0; i < atoms.length; i++) {
            ids[i] = atoms[i].getId();
        }
        return ids;
    }
}
