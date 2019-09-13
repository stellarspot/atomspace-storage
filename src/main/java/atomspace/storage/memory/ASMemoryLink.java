package atomspace.storage.memory;

import atomspace.storage.ASAtom;
import atomspace.storage.ASLink;
import atomspace.storage.ASOutgoingList;

public class ASMemoryLink extends ASMemoryAtom implements ASLink {

    final ASOutgoingList outgoingList;

    public ASMemoryLink(long id, String type, ASAtom... atoms) {
        super(id, type);
        this.outgoingList = new ASMemoryOutgoingList(atoms);
    }

    @Override
    public ASOutgoingList getOutgoingList() {
        return outgoingList;
    }

    @Override
    public String toString() {
        return toString(this);
    }

    static class ASMemoryOutgoingList implements ASOutgoingList {
        final ASAtom[] atoms;

        public ASMemoryOutgoingList(ASAtom[] atoms) {
            this.atoms = atoms;
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
