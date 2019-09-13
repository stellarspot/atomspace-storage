package atomspace.storage.neo4j;

import atomspace.storage.ASAtom;
import atomspace.storage.ASLink;
import atomspace.storage.ASOutgoingList;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

public class ASNeo4jLink extends ASNeo4jAtom implements ASLink {

    final ASNeo4jOutgoingList outgoingList;

    public ASNeo4jLink(Node node) {
        super(node);
        this.outgoingList = new ASNeo4jOutgoingList(node);
    }

    @Override
    public ASOutgoingList getOutgoingList() {
        return outgoingList;
    }

    @Override
    public String toString() {
        return toString(this);
    }

    static class ASNeo4jOutgoingList implements ASOutgoingList {
        final ASAtom[] atoms;

        public ASNeo4jOutgoingList(Node link) {

            long[] ids = (long[]) link.getProperty("ids");
            this.atoms = new ASAtom[ids.length];

            GraphDatabaseService graph = link.getGraphDatabase();

            for (int i = 0; i < ids.length; i++) {
                long id = ids[i];
                atoms[i] = ASNeo4jAtom.getAtom(graph.getNodeById(id));
            }
        }

        @Override
        public int getSize() {
            return atoms.length;
        }

        @Override
        public ASAtom getAtom(int index) {
            return atoms[index];
        }
    }
}
