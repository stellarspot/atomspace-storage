package atomspace.storage.relationaldb;

import atomspace.storage.ASAtom;
import atomspace.storage.ASLink;
import atomspace.storage.ASOutgoingList;
import atomspace.storage.AtomspaceStorageTransaction;

import java.sql.Connection;

public class ASRelationalDBLink extends ASRelationalDBAtom implements ASLink {

    final ASRelationalDBOutgoingList outgoingList;

    public ASRelationalDBLink(Connection connection, long id, String type, int arity) {
        super(connection, id, type);
        this.outgoingList = new ASRelationalDBOutgoingList(id, arity);
    }

    public ASRelationalDBLink(Connection connection, long id, String type, long... ids) {
        super(connection, id, type);
        this.outgoingList = new ASRelationalDBOutgoingList(id, ids);
    }

    public ASRelationalDBLink(Connection connection, long id, String type, ASAtom... atoms) {
        super(connection, id, type);
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
        public int getArity(AtomspaceStorageTransaction tx) {
            return arity;
        }

        @Override
        public ASAtom getAtom(AtomspaceStorageTransaction tx, int index) {
            if (!isInitialized) {
                ids = tx.getIds(id);
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
