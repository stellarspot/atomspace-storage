package atomspace.storage.neo4j;

import atomspace.storage.ASAbstractHelperTest;
import atomspace.storage.AtomspaceStorage;
import atomspace.storage.AtomspaceStorageTransaction;
import atomspace.storage.util.AtomspaceStorageHelper;

public class ASNeo4jHelperTest extends ASAbstractHelperTest {

    @Override
    public AtomspaceStorage getStorage() {
        return ASNeo4jTestUtils.getTestStorage();
    }

    @Override
    public AtomspaceStorageHelper getHelper(AtomspaceStorageTransaction tx) {
        return ASNeo4jTestUtils.getStorageHelper((AtomspaceNeo4jStorageTransaction) tx);
    }
}
