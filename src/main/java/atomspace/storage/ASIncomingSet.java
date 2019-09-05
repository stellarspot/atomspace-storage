package atomspace.storage;

import java.util.Iterator;

public interface ASIncomingSet {

    void add(ASLink link, int position);

    void remove(ASLink link, int position);

    Iterator<ASLink> getIncomingSet(String type, int position);
}
