package atomspace.storage;

import java.io.Closeable;
import java.util.Iterator;

public interface AtomspaceStorageTransaction extends Closeable {

    ASAtom get(String type, String value);

    ASAtom get(String type, ASAtom... atoms);

    default ASAtom get(long id) {
        throw new UnsupportedOperationException("Get atom by id.");
    }

    default long[] getIds(long id) {
        throw new UnsupportedOperationException("Get ids by id.");
    }

    Iterator<ASAtom> getAtoms();

    void commit();
}
