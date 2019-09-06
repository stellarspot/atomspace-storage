package atomspace.storage;

import java.util.Iterator;

public interface ASIncomingSet {

    void add(ASLink link, int position);

    void remove(ASLink link, int position);

    int getIncomingSetSize(String type, int position);

    Iterator<ASLink> getIncomingSet(String type, int position);
}
