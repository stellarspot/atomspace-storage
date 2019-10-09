package atomspace.storage.relationaldb;

import atomspace.storage.ASAtom;
import atomspace.storage.ASIncomingSet;
import atomspace.storage.ASLink;
import atomspace.storage.AtomspaceStorageTransaction;

import java.sql.Connection;
import java.util.Iterator;


public abstract class ASRelationalDBAtom implements ASAtom {

    final Connection connection;
    final long id;
    final String type;
    final ASIncomingSet incomingSet = new ASRelationalDBIncomingSet();

    public ASRelationalDBAtom(Connection connection, long id, String type) {
        this.connection = connection;
        this.id = id;
        this.type = type;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public ASIncomingSet getIncomingSet() {
        return incomingSet;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof ASRelationalDBAtom) {
            ASRelationalDBAtom that = (ASRelationalDBAtom) obj;
            return this.getId() == that.getId();
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(getId());
    }

    class ASRelationalDBIncomingSet implements ASIncomingSet {

        @Override
        public int getIncomingSetSize(AtomspaceStorageTransaction tx, String type, int arity, int position) {
            long id = ASRelationalDBAtom.this.getId();
            return tx.getIncomingSetSize(id, type, arity, position);
        }

        @Override
        public Iterator<ASLink> getIncomingSet(AtomspaceStorageTransaction tx, String type, int arity, int position) {
            long id = ASRelationalDBAtom.this.getId();
            return tx.getIncomingSet(id, type, arity, position);
        }
    }
}
