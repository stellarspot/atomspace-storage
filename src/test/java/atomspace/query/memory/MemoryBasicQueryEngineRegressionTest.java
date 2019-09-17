package atomspace.query.memory;

import atomspace.query.AbstractBasicQueryEngineRegressionTest;
import atomspace.storage.AtomspaceStorage;
import atomspace.storage.memory.ASMemoryTestUtils;

public class MemoryBasicQueryEngineRegressionTest extends AbstractBasicQueryEngineRegressionTest {

    @Override
    protected AtomspaceStorage getStorage() {
        return ASMemoryTestUtils.getTestStorage();
    }

}
