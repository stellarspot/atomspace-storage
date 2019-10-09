package atomspace.storage.relationaldb;

import atomspace.storage.ASAtom;
import atomspace.storage.ASLink;
import atomspace.storage.ASOutgoingList;
import atomspace.storage.AtomspaceStorageTransaction;

import java.sql.Connection;

public class ASRelationalDBLink extends ASRelationalDBAtom implements ASLink {

    final ASRelationalDBOutgoingList outgoingList;

    public ASRelationalDBLink(Connection connection, long id, String type, ASAtom... atoms) {
        super(connection, id, type);
        this.outgoingList = new ASRelationalDBOutgoingList(atoms);
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

        final ASAtom[] atoms;

        public ASRelationalDBOutgoingList(ASAtom... atoms) {
            this.atoms = atoms;
        }

        @Override
        public int getArity(AtomspaceStorageTransaction tx) {
            return atoms.length;
        }

        @Override
        public ASAtom getAtom(AtomspaceStorageTransaction tx, int index) {
            return atoms[index];
        }

        @Override
        public String toString() {
            return toString(atoms);
        }
    }
}
