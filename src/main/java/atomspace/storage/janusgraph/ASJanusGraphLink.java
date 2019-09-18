package atomspace.storage.janusgraph;

import atomspace.storage.ASAtom;
import atomspace.storage.ASLink;
import atomspace.storage.ASOutgoingList;
import atomspace.storage.neo4j.ASNeo4jAtom;
import org.janusgraph.core.JanusGraphTransaction;
import org.janusgraph.core.JanusGraphVertex;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

public class ASJanusGraphLink extends ASJanusGraphAtom implements ASLink {

    final ASJanusGraphOutgoingList outgoingList;

    public ASJanusGraphLink(JanusGraphVertex vertex) {
        super(vertex);
        this.outgoingList = new ASJanusGraphOutgoingList(vertex);
    }

    @Override
    public ASOutgoingList getOutgoingList() {
        return outgoingList;
    }

    @Override
    public String toString() {
        return toString(this);
    }

    static class ASJanusGraphOutgoingList implements ASOutgoingList {
        final ASAtom[] atoms;

        public ASJanusGraphOutgoingList(JanusGraphVertex vertex) {

            long[] ids = (long[]) vertex.property("ids").value();
            this.atoms = new ASAtom[ids.length];

            JanusGraphTransaction graph = vertex.graph();

            for (int i = 0; i < ids.length; i++) {
                long id = ids[i];
                atoms[i] = ASJanusGraphAtom.getAtom(graph.getVertex(id));
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
