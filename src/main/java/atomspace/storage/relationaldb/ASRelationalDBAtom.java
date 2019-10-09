package atomspace.storage.relationaldb;

import atomspace.storage.ASAtom;
import atomspace.storage.ASIncomingSet;
import atomspace.storage.basic.ASBasicIncomingSet;


public abstract class ASRelationalDBAtom implements ASAtom {

    final long id;
    final String type;
    final ASIncomingSet incomingSet;

    public ASRelationalDBAtom(long id, String type) {
        this.id = id;
        this.type = type;
        this.incomingSet = new ASBasicIncomingSet(id);
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
}
