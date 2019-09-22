package atomspace.storage.relationaldb;

import atomspace.storage.ASNode;
import atomspace.storage.neo4j.ASNeo4jAtom;
import org.neo4j.graphdb.Node;

import java.sql.Connection;

public class ASRelationalDBNode extends ASRelationalDBAtom implements ASNode {

    final String value;

    public ASRelationalDBNode(Connection connection, long id, String type, String value) {
        super(connection, id, type);
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return toString(this);
    }
}
