package atomspace.storage.janusgraph;

import atomspace.storage.ASNode;
import atomspace.storage.neo4j.ASNeo4jAtom;
import org.janusgraph.core.JanusGraphVertex;
import org.neo4j.graphdb.Node;

public class ASJanusGraphNode extends ASJanusGraphAtom implements ASNode {


    public ASJanusGraphNode(JanusGraphVertex vertex) {
        super(vertex);
    }

    @Override
    public String getValue() {
        return vertex.property("value").value().toString();
    }

    @Override
    public String toString() {
        return toString(this);
    }
}
