package atomspace.storage.memory;

import atomspace.storage.ASAtom;
import atomspace.storage.ASLink;
import atomspace.storage.ASOutgoingList;

import java.util.Objects;

public class ASMemoryLink extends ASMemoryAtom implements ASLink {

    final ASOutgoingList outgoingList;

    public ASMemoryLink(long id, String type, ASAtom... atoms) {
        super(id, type);
        this.outgoingList = new ASMemoryOutgoingList(atoms);

        for (int i = 0; i < atoms.length; i++) {
            atoms[i].getIncomingSet().add(this, i);
        }
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

    static class AtomWithPosition {
        final ASAtom atom;
        final int position;

        public AtomWithPosition(ASAtom atom, int position) {
            this.atom = atom;
            this.position = position;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if ((o instanceof AtomWithPosition)) {
                AtomWithPosition that = (AtomWithPosition) o;
                return this.position == that.position &&
                        Objects.equals(this.atom, that.atom);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(atom, position);
        }
    }
}
