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

        private final Map<String, Map<Integer, Set<ASLink>>> typeAndPositionMap = new HashMap<>();

        @Override
        public void add(ASLink link, int position) {

            Map<Integer, Set<ASLink>> positionsMap =
                    typeAndPositionMap.computeIfAbsent(link.getType(), (key) -> new HashMap<>());

            Set<ASLink> links = positionsMap.computeIfAbsent(position, (key) -> new HashSet<>());
            links.add(link);
        }

        @Override
        public void remove(ASLink link, int position) {
        }

        @Override
        public Iterator<ASLink> getIncomingSet(String type, int position) {

            Map<Integer, Set<ASLink>> positionsMap = typeAndPositionMap.get(type);

            if (positionsMap == null) {
                return Collections.emptyIterator();
            }

            Set<ASLink> links = positionsMap.get(position);

            if (links == null) {
                return Collections.emptyIterator();
            }

            return links.iterator();
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
