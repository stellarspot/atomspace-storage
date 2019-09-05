package atomspace.storage.memory;

import atomspace.storage.ASAtom;
import atomspace.storage.ASIncomingSet;
import atomspace.storage.ASLink;

import java.util.*;

public abstract class ASMemoryAtom implements ASAtom {

    public final long id;
    public final String type;

    final ASIncomingSet incomingSet;

    public ASMemoryAtom(long id, String type) {
        this.id = id;
        this.type = type;
        this.incomingSet = new ASMemoryIncomingSet();
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public ASIncomingSet getIncomingSet() {
        return incomingSet;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof ASMemoryAtom) {
            ASMemoryAtom that = (ASMemoryAtom) obj;
            return this.id == that.id;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }

    static class ASMemoryIncomingSet implements ASIncomingSet {

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
}
