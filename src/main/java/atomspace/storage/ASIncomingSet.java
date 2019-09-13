package atomspace.storage;

import java.util.Iterator;

public interface ASIncomingSet {

    void add(ASLink link, int size, int position);

    void remove(ASLink link, int size, int position);

    int getIncomingSetSize(String type, int size, int position);

    Iterator<ASLink> getIncomingSet(String type, int size, int position);
}
