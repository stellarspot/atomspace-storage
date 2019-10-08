package atomspace.storage.janusgraph;

import atomspace.storage.ASAtom;
import atomspace.storage.ASIncomingSet;
import atomspace.storage.ASLink;
import atomspace.storage.AtomspaceStorageTransaction;
import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraphVertex;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class ASJanusGraphAtom implements ASAtom {

    final Vertex vertex;
    final ASIncomingSet incomingSet;


    public ASJanusGraphAtom(Vertex vertex) {
        this.vertex = vertex;
        this.incomingSet = new ASJanusGraphIncomingSet();
    }

    @Override
    public long getId() {
        return (long) vertex.id();
    }

    @Override
    public String getType() {
        return vertex.property("type").value().toString();
    }

    @Override
    public ASIncomingSet getIncomingSet() {
        return incomingSet;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof ASJanusGraphAtom) {
            ASJanusGraphAtom that = (ASJanusGraphAtom) obj;
            return this.getId() == that.getId();
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(getId());
    }

    public static ASJanusGraphAtom getAtom(JanusGraphVertex vertex) {
        String kind = vertex.property("kind").value().toString();

        switch (kind) {
            case "Node":
                return new ASJanusGraphNode(vertex);
            case "Link":
                return new ASJanusGraphLink(vertex);
            default:
                throw new RuntimeException(String.format("Unknown vertex kind: %s", kind));
        }
    }

    class ASJanusGraphIncomingSet implements ASIncomingSet {

        public void add(ASLink link, int arity, int position) {
            Vertex parent = ((ASJanusGraphLink) link).vertex;
            String key = getKey(link.getType(), arity, position);
            vertex.addEdge(key, parent);
        }

        @Override
        public int getIncomingSetArity(AtomspaceStorageTransaction tx, String type, int arity, int position) {

            // TBD: use the count store
            int s = 0;
            Iterator<Edge> iter = getSet(type, arity, position);
            for (; iter.hasNext(); iter.next()) {
                s++;
            }
            return s;
        }

        @Override
        public Iterator<ASLink> getIncomingSet(AtomspaceStorageTransaction tx, String type, int arity, int position) {

            List<ASLink> links = new ArrayList<>();
            Iterator<Edge> iter = getSet(type, arity, position);
            while (iter.hasNext()) {
                JanusGraphVertex vertex = (JanusGraphVertex) iter.next().inVertex();
                links.add(new ASJanusGraphLink(vertex));
            }

            return links.iterator();
        }

        private Iterator<Edge> getSet(String type, int size, int position) {
            String key = getKey(type, size, position);
            return vertex.edges(Direction.OUT, key);
        }

        private String getKey(String type, int size, int position) {
            return String.format("%s_%d_%d", type, size, position);
        }
    }
}
