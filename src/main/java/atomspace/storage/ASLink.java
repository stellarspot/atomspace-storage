package atomspace.storage;

public interface ASLink extends ASAtom {

    ASIncomingSet getIncomingSet();

    ASOutgoingList getOutgoingList();

}
