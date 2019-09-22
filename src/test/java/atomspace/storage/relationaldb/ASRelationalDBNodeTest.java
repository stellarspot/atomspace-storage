package atomspace.storage.relationaldb;

import atomspace.storage.ASAbstractNodeTest;
import atomspace.storage.AtomspaceStorage;

public class ASRelationalDBNodeTest extends ASAbstractNodeTest {

    @Override
    protected AtomspaceStorage getStorage() {
        return ASRelationalDBTestUtils.getTestStorage();
    }
}
