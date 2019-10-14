package atomspace.storage.janusgraph;

import atomspace.storage.ASAbstractTransactionTest;
import atomspace.storage.AtomspaceStorage;
import org.junit.Ignore;

@Ignore
public class ASJanusGraphTransactionTest extends ASAbstractTransactionTest {

    @Override
    public AtomspaceStorage getStorage() {
        return ASJanusGraphTestUtils.getTestStorage();
    }
}
