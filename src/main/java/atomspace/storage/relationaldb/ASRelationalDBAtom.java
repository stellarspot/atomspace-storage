package atomspace.storage.relationaldb;

import atomspace.storage.ASAtom;
import atomspace.storage.ASIncomingSet;
import atomspace.storage.ASLink;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class ASRelationalDBAtom implements ASAtom {

    final Connection connection;
    final long id;
    final String type;
    final ASIncomingSet incomingSet = null;


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
        public void add(ASLink link, int size, int position) {
        }

        @Override
        public void remove(ASLink link, int size, int position) {
        }

        @Override
        public int getIncomingSetSize(String type, int size, int position) {
            return 0;
        }

        @Override
        public Iterator<ASLink> getIncomingSet(String type, int size, int position) {

            List<ASLink> links = new ArrayList<>();
            return links.iterator();
        }
    }
}
