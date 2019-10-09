package atomspace.storage.neo4j;

import atomspace.storage.ASAbstractHelperTest;
import atomspace.storage.AtomspaceStorage;
import atomspace.storage.ASTransaction;
import atomspace.storage.util.AtomspaceStorageHelper;

public class ASNeo4jHelperTest extends ASAbstractHelperTest {

    @Override
    public AtomspaceStorage getStorage() {
        return ASNeo4jTestUtils.getTestStorage();
    }

    @Override
    public AtomspaceStorageHelper getHelper(ASTransaction tx) {
        return ASNeo4jTestUtils.getStorageHelper((ASNeo4jTransaction) tx);
    }
}
