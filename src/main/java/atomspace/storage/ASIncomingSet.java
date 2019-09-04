package atomspace.storage;

import java.util.Iterator;

public interface ASIncomingSet {

    void add(ASAtom atom, int position);

    void remove(ASAtom atom, int position);

    Iterator<ASAtom> getIncomingSet(String type, int position);
}
