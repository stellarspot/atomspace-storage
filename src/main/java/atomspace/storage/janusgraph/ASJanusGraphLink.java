package atomspace.storage.janusgraph;

import atomspace.storage.ASAtom;
import atomspace.storage.ASLink;
import atomspace.storage.ASOutgoingList;
import atomspace.storage.ASTransaction;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraphTransaction;
import org.janusgraph.core.JanusGraphVertex;

public class ASJanusGraphLink extends ASJanusGraphAtom implements ASLink {

    final ASJanusGraphOutgoingList outgoingList;

    public ASJanusGraphLink(Vertex vertex) {
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

        public ASJanusGraphOutgoingList(Vertex vertex) {

            long[] ids = (long[]) vertex.property("ids").value();
            this.atoms = new ASAtom[ids.length];

            JanusGraphTransaction graph = ((JanusGraphVertex) vertex).graph();

            for (int i = 0; i < ids.length; i++) {
                long id = ids[i];
                atoms[i] = ASJanusGraphAtom.getAtom(graph.getVertex(id));
            }
        }

        @Override
        public int getArity(ASTransaction tx) {
            return atoms.length;
        }

        @Override
        public ASAtom getAtom(ASTransaction tx, int index) {
            return atoms[index];
        }

        @Override
        public String toString() {
            return toString(atoms);
        }
    }
}
