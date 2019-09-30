package atomspace.storage;

import java.util.Iterator;

public interface ASIncomingSet {

    int getIncomingSetArity(String type, int arity, int position);

    Iterator<ASLink> getIncomingSet(String type, int arity, int position);
}
