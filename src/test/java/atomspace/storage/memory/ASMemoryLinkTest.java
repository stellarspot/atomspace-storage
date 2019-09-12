package atomspace.storage.memory;

import atomspace.storage.ASAbstractLinkTest;
import atomspace.storage.AtomspaceStorage;

public class ASMemoryLinkTest extends ASAbstractLinkTest {

    @Override
    protected AtomspaceStorage getStorage() {
        return ASMemoryTestUtils.getTestStorage();
    }
}
