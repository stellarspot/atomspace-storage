package atomspace.storage.relationaldb;

import atomspace.storage.ASAbstractIncomingSetTest;
import atomspace.storage.AtomspaceStorage;
import org.junit.Test;

public class ASRelationalDBIncomingSetTest extends ASAbstractIncomingSetTest {

    @Override
    protected AtomspaceStorage getStorage() {
        return ASRelationalDBTestUtils.getTestStorage();
    }
}
