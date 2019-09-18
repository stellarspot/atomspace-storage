package atomspace.storage.memory;

import atomspace.storage.ASAbstractHelperTest;
import atomspace.storage.AtomspaceStorage;
import atomspace.storage.AtomspaceStorageTransaction;
import atomspace.storage.util.AtomspaceStorageHelper;

public class ASMemoryHelperTest extends ASAbstractHelperTest {

    @Override
    public AtomspaceStorage getStorage() {
        return ASMemoryTestUtils.getTestStorage();
    }

    @Override
    public AtomspaceStorageHelper getHelper(AtomspaceStorageTransaction tx) {
        return ASMemoryTestUtils.getStorageHelper((AtomspaceMemoryStorageTransaction) tx);
    }
}
