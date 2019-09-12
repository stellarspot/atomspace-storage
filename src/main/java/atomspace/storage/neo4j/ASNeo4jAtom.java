package atomspace.storage.neo4j;

import atomspace.storage.ASAtom;
import atomspace.storage.ASIncomingSet;
import org.neo4j.graphdb.Node;

public abstract class ASNeo4jAtom implements ASAtom {

    final Node node;

    public ASNeo4jAtom(Node node) {
        this.node = node;
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
        return null;
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
}
