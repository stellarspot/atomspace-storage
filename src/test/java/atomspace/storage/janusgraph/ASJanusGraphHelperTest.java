package atomspace.storage.janusgraph;

import atomspace.storage.ASAbstractHelperTest;
import atomspace.storage.AtomspaceStorage;
import atomspace.storage.AtomspaceStorageTransaction;
import atomspace.storage.memory.ASMemoryTestUtils;
import atomspace.storage.memory.AtomspaceMemoryStorageTransaction;
import atomspace.storage.util.AtomspaceStorageHelper;

public class ASJanusGraphHelperTest extends ASAbstractHelperTest {

    @Override
    public AtomspaceStorage getStorage() {
        return ASJanusGraphTestUtils.getTestStorage();
    }

    @Override
    public AtomspaceStorageHelper getHelper(AtomspaceStorageTransaction tx) {
        return ASJanusGraphTestUtils.getStorageHelper((AtomSpaceJanusGraphStorageTransaction) tx);
    }
}