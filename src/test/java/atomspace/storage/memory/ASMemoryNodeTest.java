package atomspace.storage.memory;

import atomspace.storage.ASAbstractNodeTest;
import atomspace.storage.AtomspaceStorage;

public class ASMemoryNodeTest extends ASAbstractNodeTest {

    @Override
    public AtomspaceStorage getStorage() {
        return new AtomspaceMemoryStorage();
    }
}
