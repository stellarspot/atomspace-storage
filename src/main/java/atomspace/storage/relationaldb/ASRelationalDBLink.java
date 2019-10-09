package atomspace.storage.relationaldb;

import atomspace.storage.ASAtom;
import atomspace.storage.ASLink;
import atomspace.storage.ASOutgoingList;
import atomspace.storage.ASTransaction;

public class ASRelationalDBLink extends ASRelationalDBAtom implements ASLink {

    final ASRelationalDBOutgoingList outgoingList;

    public ASRelationalDBLink(long id, String type, int arity) {
        super(id, type);
        this.outgoingList = new ASRelationalDBOutgoingList(id, arity);
    }

    public ASRelationalDBLink(long id, String type, long... ids) {
        super(id, type);
        this.outgoingList = new ASRelationalDBOutgoingList(id, ids);
    }

    public ASRelationalDBLink(long id, String type, ASAtom... atoms) {
        super(id, type);
        this.outgoingList = new ASRelationalDBOutgoingList(id, atoms);
    }

    @Override
    public ASOutgoingList getOutgoingList() {
        return outgoingList;
    }

    @Override
    public String toString() {
        return toString(this);
    }

    static class ASRelationalDBOutgoingList implements ASOutgoingList {

        private final long id;
        private final int arity;
        private long[] ids;
        private ASAtom[] atoms;
        private boolean isInitialized = false;

        public ASRelationalDBOutgoingList(long id, int arity) {
            this.id = id;
            this.arity = arity;
        }

        public ASRelationalDBOutgoingList(long id, long... ids) {
            this(id, ids.length);
            this.ids = ids;
            this.atoms = new ASAtom[ids.length];
            this.isInitialized = true;
        }

        public ASRelationalDBOutgoingList(long id, ASAtom... atoms) {
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
}
