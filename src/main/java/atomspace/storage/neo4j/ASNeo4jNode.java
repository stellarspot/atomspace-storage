package atomspace.storage.neo4j;

import atomspace.storage.ASNode;
import org.neo4j.graphdb.Node;

public class ASNeo4jNode extends ASNeo4jAtom implements ASNode {


    public ASNeo4jNode(Node node) {
        super(node);
    }

    @Override
    public String getValue() {
        return node.getProperty("value").toString();
    }

    @Override
    public String toString() {
        return toString(this);
    }
}
