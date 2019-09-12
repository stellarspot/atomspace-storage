package atomspace.storage;

import java.io.Closeable;
import java.util.Iterator;

public interface AtomspaceStorageTransaction extends Closeable {

    ASAtom get(String type, String value);

    ASAtom get(String type, ASAtom... atoms);

    Iterator<ASAtom> getAtoms();

    void commit();
}
