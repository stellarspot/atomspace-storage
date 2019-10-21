package atomspace.layer.gremlin;

import atomspace.storage.ASAtom;
import atomspace.storage.ASLink;
import atomspace.storage.ASNode;
import atomspace.storage.base.ASBaseLink;
import atomspace.storage.base.ASBaseNode;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import static atomspace.storage.util.AtomspaceStorageUtils.getKey;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.addV;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.unfold;

public class ASGremlinOneRequestTransaction extends ASAbstractGremlinTransaction {

    public ASGremlinOneRequestTransaction(AtomspaceGremlinStorage.Storage storage) {
        super(storage);
        System.out.printf("Create ASGremlinOneRequestTransaction%n");
    }

    @Override
    public ASNode get(String type, String value) {

        GraphTraversal<Object, Vertex> addVertex = addV(LABEL_NODE)
                .property(KIND, LABEL_NODE)
                .property(TYPE, type)
                .property(VALUE, value);

        if (useCustomIds) {
            addVertex = addVertex.property(T.id, storage.getNextId());
        }

        Vertex v = g.V()
                .hasLabel(LABEL_NODE)
                .has(TYPE, type)
                .has(VALUE, value)
                .fold()
                .coalesce(unfold(), addVertex)
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

        if (useCustomIds) {
            addVertex = addVertex.property(T.id, storage.getNextId());
        }

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
}
