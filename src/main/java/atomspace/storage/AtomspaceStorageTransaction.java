package atomspace.storage;

import java.io.Closeable;
import java.util.Iterator;

public interface AtomspaceStorageTransaction extends Closeable {

    /**
     * Gets or create Node
     *
     * @param type
     * @param value
     * @return
     */
    ASAtom get(String type, String value);

    /**
     * Gets or create Link
     *
     * @param type
     * @param atoms
     * @return
     */
    ASAtom get(String type, ASAtom... atoms);

    /**
     * Gets an atom by id
     *
     * @param id
     * @return
     */
    default ASAtom get(long id) {
        throw new UnsupportedOperationException("Get atom by id.");
    }

    /**
     * Gets ids of the atom outgoing list by the given atom id
     *
     * @param id
     * @return
     */
    default long[] getOutgoingListIds(long id) {
        throw new UnsupportedOperationException("Get ids by id.");
    }

    /**
     * Gets size of the atom incoming set by the given id
     *
     * @param id
     * @param type
     * @param arity
     * @param position
     * @return
     */
    default int getIncomingSetSize(long id, String type, int arity, int position) {
        throw new UnsupportedOperationException("Get incoming set arity by id.");
    }

    /**
     * Gets the atom incoming set by the given id
     *
     * @param id
     * @param type
     * @param arity
     * @param position
     * @return
     */
    default Iterator<ASLink> getIncomingSet(long id, String type, int arity, int position) {
        throw new UnsupportedOperationException("Get incoming set by id.");
    }

    /**
     * Returns all atoms
     *
     * @return
     */
    Iterator<ASAtom> getAtoms();

    /**
     * Commits the current transaction
     */
    void commit();
}
