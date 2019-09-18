package atomspace.storage.janusgraph;

import atomspace.storage.ASAtom;
import atomspace.storage.ASIncomingSet;
import atomspace.storage.ASLink;
import atomspace.storage.neo4j.ASNeo4jLink;
import atomspace.storage.neo4j.ASNeo4jNode;
import org.janusgraph.core.JanusGraphVertex;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class ASJanusGraphAtom implements ASAtom {

    final JanusGraphVertex vertex;
    final ASIncomingSet incomingSet;


    public ASJanusGraphAtom(JanusGraphVertex vertex) {
        this.vertex = vertex;
        this.incomingSet = null;
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
        String kind = vertex.property("kind").toString();

        switch (kind) {
            case "Node":
                return new ASJanusGraphNode(vertex);
            case "Link":
                return new ASJanusGraphLink(vertex);
            default:
                throw new RuntimeException(String.format("Unknown vertex kind: %s", kind));
        }
    }
}
