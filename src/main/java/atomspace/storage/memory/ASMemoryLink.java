package atomspace.storage.memory;

import atomspace.storage.ASAtom;
import atomspace.storage.ASIncomingSet;
import atomspace.storage.ASLink;
import atomspace.storage.ASOutgoingList;

import java.util.*;

public class ASMemoryLink extends ASMemoryAtom implements ASLink {

    final ASIncomingSet incomingSet;
    final ASOutgoingList outgoingList;

    public ASMemoryLink(long id, String type, ASAtom... atoms) {
        super(id, type);
        this.incomingSet = new ASMemoryincomingSet();
        this.outgoingList = new ASMemoryOutgoingList(atoms);
    }

    @Override
    public ASIncomingSet getIncomingSet() {
        return incomingSet;
    }

    @Override
    public ASOutgoingList getOutgoingList() {
        return outgoingList;
    }

    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder();
        builder.append(type).append("(");

        for (int i = 0; i < outgoingList.getSize(); i++) {
            if (i != 0) {
                builder.append(",");
            }
            builder.append(outgoingList.getAtom(i));
        }

        builder.append(")");
        return builder.toString();
    }

    static class ASMemoryincomingSet implements ASIncomingSet {

        private final Set<AtomWithPosition> items = new HashSet<>();

        @Override
        public void add(ASAtom atom, int position) {
            items.add(new AtomWithPosition(atom, position));
        }

        @Override
        public void remove(ASAtom atom, int position) {
            items.remove(new AtomWithPosition(atom, position));
        }

        @Override
        public Iterator<ASAtom> getIncomingSet(String type, int position) {

            List<ASAtom> atoms = new ArrayList<>();
            for (AtomWithPosition item : items) {
                ASAtom atom = item.atom;

                if (type.equals(atom.getType())) {
                    if (position == -1 || position == item.position) {
                        atoms.add(atom);
                    }
                }
            }

            return atoms.iterator();
        }
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
