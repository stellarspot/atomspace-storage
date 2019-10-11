package atomspace.storage;

import java.io.Closeable;
import java.util.Iterator;

/**
 * ASTransaction contains main methods to create and retrieve atoms
 * from the underlined storage.
 * <p>
 * Note: depending on the underlined storage some methods may not be
 * supported and throw UnsupportedOperationException
 *
 * @see UnsupportedOperationException
 */
public interface ASTransaction extends Closeable {

    /**
     * Gets or creates Node by the given type and value.
     *
     * @param type  the type of the atom
     * @param value the value of the atom
     * @return Node
     */
    ASAtom get(String type, String value);

    /**
     * Gets or creates Link by the given type and outgoing list.
     *
     * @param type  the type of the atom
     * @param atoms the outgoing list of the atom
     * @return List
     */
    ASAtom get(String type, ASAtom... atoms);

    /**
     * Gets the atom by for the unique identifier.
     *
     * @param id the id of the atom
     * @return Atom
     */
    ASAtom get(long id);

    /**
     * Gets ids of the atom outgoing list by the given atom id
     *
     * @param id the id of the atom
     * @return outgoing list ids
     */
    long[] getOutgoingListIds(long id);

    /**
     * Gets size of the atom incoming set by the given id
     *
     * @param id       the id of the atom
     * @param type     the type of the atom
     * @param arity    the arity of the atom
     * @param position the index of the atom in the parent Link
     * @return size of the incoming set
     */
    int getIncomingSetSize(long id, String type, int arity, int position);

    /**
     * Gets the incoming set of the atom by the given id
     *
     * @param id       the id of the atom
     * @param type     the type of the atom
     * @param arity    the arity of the atom
     * @param position the index of the atom in the parent Link
     * @return incoming set
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
