package atomspace.storage;

import java.io.Closeable;
import java.util.Iterator;

/**
 * ASTransaction contains main methods to create and query atoms
 * from underlined storage.
 * <p>
 * Note: depending on the underlined storage some methods may not be
 * supported and they throw UnsupportedOperationException
 *
 * @see UnsupportedOperationException
 */
public interface ASTransaction extends Closeable {

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
    ASAtom get(long id);

    /**
     * Gets ids of the atom outgoing list by the given atom id
     *
     * @param id
     * @return
     */
    long[] getOutgoingListIds(long id);

    /**
     * Gets size of the atom incoming set by the given id
     *
     * @param id
     * @param type
     * @param arity
     * @param position
     * @return
     */
    int getIncomingSetSize(long id, String type, int arity, int position);

    /**
     * Gets the atom incoming set by the given id
     *
     * @param id
     * @param type
     * @param arity
     * @param position
     * @return
     */
    Iterator<ASLink> getIncomingSet(long id, String type, int arity, int position);

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
