package atomspace.storage.neo4j;

import atomspace.storage.ASAtom;
import atomspace.storage.ASIncomingSet;
import atomspace.storage.ASLink;
import org.neo4j.graphdb.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class ASNeo4jAtom implements ASAtom {

    final Node node;
    final ASIncomingSet incomingSet;


    public ASNeo4jAtom(Node node) {
        this.node = node;
        this.incomingSet = new ASNeo4jIncomingSet();
    }

    @Override
    public long getId() {
        return node.getId();
    }

    @Override
    public String getType() {
        return node.getProperty("type").toString();
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

        if (obj instanceof ASNeo4jAtom) {
            ASNeo4jAtom that = (ASNeo4jAtom) obj;
            return this.getId() == that.getId();
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(getId());
    }

    public static ASNeo4jAtom getAtom(Node node) {
        String kind = node.getProperty("kind").toString();

        switch (kind) {
            case "Node":
                return new ASNeo4jNode(node);
            case "Link":
                return new ASNeo4jLink(node);
            default:
                throw new RuntimeException(String.format("Unknown node kind: %s", kind));
        }
    }

    class ASNeo4jIncomingSet implements ASIncomingSet {

        @Override
        public void add(ASLink link, int arity, int position) {
            Node parent = ((ASNeo4jAtom) link).node;
            String key = getKey(link.getType(), arity, position);
            node.createRelationshipTo(parent, RelationshipType.withName(key));
        }

        @Override
        public void remove(ASLink link, int arity, int position) {
        }

        @Override
        public int getIncomingSetArity(String type, int arity, int position) {

            // TBD: use the count store
            int s = 0;
            for (Relationship r : getSet(type, arity, position)) {
                s++;
            }
            return s;
        }

        @Override
        public Iterator<ASLink> getIncomingSet(String type, int arity, int position) {

            List<ASLink> links = new ArrayList<>();
            for (Relationship r : getSet(type, arity, position)) {
                Node parent = r.getEndNode();
                links.add(new ASNeo4jLink(parent));
            }

            return links.iterator();
        }

        private Iterable<Relationship> getSet(String type, int size, int position) {
            String key = getKey(type, size, position);
            return node.getRelationships(RelationshipType.withName(key), Direction.OUTGOING);
        }

        private String getKey(String type, int size, int position) {
            return String.format("%s_%d_%d", type, size, position);
        }
    }
}
