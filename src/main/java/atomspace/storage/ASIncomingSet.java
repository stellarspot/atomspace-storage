package atomspace.storage;

import java.util.Iterator;

public interface ASIncomingSet {

    void add(ASLink link, int arity, int position);

    void remove(ASLink link, int arity, int position);

    int getIncomingSetArity(String type, int arity, int position);

    Iterator<ASLink> getIncomingSet(String type, int arity, int position);
}
