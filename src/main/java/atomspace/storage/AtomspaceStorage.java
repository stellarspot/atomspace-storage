package atomspace.storage;

import java.util.Iterator;

public interface AtomspaceStorage {

    ASAtom get(String type, String value);

    ASAtom get(String type, ASAtom... atoms);

    Iterator<ASAtom> getAtoms();
}
