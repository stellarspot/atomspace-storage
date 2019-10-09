package atomspace.storage.basic;

import atomspace.storage.ASAtom;
import atomspace.storage.ASOutgoingList;
import atomspace.storage.ASTransaction;

/**
 * Default implementation of lazy ASOutgoingList.
 * <p>
 * The outgoing list is initialized eagerly by atoms during link creation.
 * <p>
 * Lazy outgoing list initialization is used when a link is retrieved
 * from outgoing set and prevents full link construction.
 * Lazy initialization is useful for atoms querying when some link outgoing
 * atoms are not matched with the query.
 */
public class ASBasicOutgoingList implements ASOutgoingList {

    private final long id;
    private final int arity;
    private long[] ids;
    private ASAtom[] atoms;
    private boolean isInitialized = false;

    public ASBasicOutgoingList(long id, int arity) {
        this.id = id;
        this.arity = arity;
    }

    public ASBasicOutgoingList(long id, long... ids) {
        this(id, ids.length);
        this.ids = ids;
        this.atoms = new ASAtom[ids.length];
        this.isInitialized = true;
    }

    public ASBasicOutgoingList(long id, ASAtom... atoms) {
        this(id, atoms.length);
        this.atoms = atoms;
        this.isInitialized = true;
    }

    @Override
    public int getArity(ASTransaction tx) {
        return arity;
    }

    @Override
    public ASAtom getAtom(ASTransaction tx, int index) {
        if (!isInitialized) {
            ids = tx.getOutgoingListIds(id);
            atoms = new ASAtom[arity];
            isInitialized = true;
        }

        ASAtom atom = atoms[index];

        if (atom == null) {
            long childId = ids[index];
            atoms[index] = atom = tx.get(childId);
        }

        return atom;
    }

    @Override
    public String toString() {
        return toString(atoms);
    }
}
