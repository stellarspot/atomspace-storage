package atomspace.storage.janusgraph;

import atomspace.storage.ASNode;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraphVertex;

public class ASJanusGraphNode extends ASJanusGraphAtom implements ASNode {


    public ASJanusGraphNode(Vertex vertex) {
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
