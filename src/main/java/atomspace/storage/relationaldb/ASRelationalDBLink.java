package atomspace.storage.relationaldb;

import atomspace.storage.ASAtom;
import atomspace.storage.ASLink;
import atomspace.storage.ASOutgoingList;
import atomspace.storage.basic.ASBasicOutgoingList;

public class ASRelationalDBLink extends ASRelationalDBAtom implements ASLink {

    final ASOutgoingList outgoingList;

    public ASRelationalDBLink(long id, String type, int arity) {
        super(id, type);
        this.outgoingList = new ASBasicOutgoingList(id, arity);
    }

    public ASRelationalDBLink(long id, String type, long... ids) {
        super(id, type);
        this.outgoingList = new ASBasicOutgoingList(id, ids);
    }

    public ASRelationalDBLink(long id, String type, ASAtom... atoms) {
        super(id, type);
        this.outgoingList = new ASBasicOutgoingList(id, atoms);
    }

    @Override
    public ASOutgoingList getOutgoingList() {
        return outgoingList;
    }

    @Override
    public String toString() {
        return toString(this);
    }
}
