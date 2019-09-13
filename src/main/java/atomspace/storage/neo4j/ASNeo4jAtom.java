package atomspace.storage.neo4j;

import atomspace.storage.ASAtom;
import atomspace.storage.ASIncomingSet;
import atomspace.storage.ASLink;
import org.neo4j.graphdb.Node;

import java.util.Iterator;

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
        return node.getLabels().iterator().next().name();
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

    static class ASNeo4jIncomingSet implements ASIncomingSet {
        @Override
        public void add(ASLink link, int position) {

        }

        @Override
        public void remove(ASLink link, int position) {

        }

        @Override
        public int getIncomingSetSize(String type, int position) {
            return 0;
        }

        @Override
        public Iterator<ASLink> getIncomingSet(String type, int position) {
            return null;
        }
    }
}
