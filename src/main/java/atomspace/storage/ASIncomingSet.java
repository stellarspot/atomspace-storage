package atomspace.storage;

import java.util.Iterator;

public interface ASIncomingSet {

    int getIncomingSetSize(AtomspaceStorageTransaction tx, String type, int arity, int position);

    Iterator<ASLink> getIncomingSet(AtomspaceStorageTransaction tx, String type, int arity, int position);
}
